/**
 * 
 */
package negocio.comuns.arquitetura.enumeradores;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoModuloEnum {
	TODOS(0, null, ""),
	ACADEMICO(1, PerfilAcessoPermissaoAcademicoEnum.values(), "/visaoAdministrativo/academico/menuAcademico.xhtml"),
	ADMINISTRATIVO(2, PerfilAcessoPermissaoAdministrativoEnum.values(), "/visaoAdministrativo/administrativo/menuAdministrativo.xhtml"),
	AVALIACAO_INSTITUCIONAL(3, PerfilAcessoPermissaoAvaliacaoInstitucionalEnum.values(), "/visaoAdministrativo/avaliacaoInstitucional/menuAvaliacaoInstitucional.xhtml"),
//	BANCO_DE_CURRICULOS(4, PerfilAcessoPermissaoBancoCurriculosEnum.values(), "/visaoAdministrativo/bancoCurriculos/menuBancoCurriculo.xhtml"),
	BIBLIOTECA(5, PerfilAcessoPermissaoBibliotecaEnum.values(), "/visaoAdministrativo/biblioteca/menuBiblioteca.xhtml"),
	COMPRAS(6, PerfilAcessoPermissaoComprasEnum.values(), "/visaoAdministrativo/compras/menuCompras.xhtml"),
	CONTABIL(7, null, ""),
	CRM(8, PerfilAcessoPermissaoCRMEnum.values(), "/visaoAdministrativo/crm/menuCRM.xhtml"),
	EAD(9, PerfilAcessoPermissaoEADEnum.values(), "/visaoAdministrativo/ead/menuEAD.xhtml"),
	ESTAGIO(10, PerfilAcessoPermissaoEstagioEnum.values(), "/visaoAdministrativo/estagio/menuEstagio.xhtml"),
	FINANCEIRO(11, PerfilAcessoPermissaoFinanceiroEnum.values(), "/visaoAdministrativo/financeiro/menuFinanceiro.xhtml"),
	NOTA_FISCAL(12, PerfilAcessoPermissaoNotaFiscalEnum.values(), "/visaoAdministrativo/notaFiscal/menuNotaFiscal.xhtml"),
	PATRIMONIO(13, PerfilAcessoPermissaoPatrimonioEnum.values(), "/visaoAdministrativo/patrimonio/menuPatrimonio.xhtml"),
	PLANO_ORCAMENTARIO(14, PerfilAcessoPermissaoPlanoOrcamentarioEnum.values(), "/visaoAdministrativo/planoOrcamentario/menuPlanoOrcamentario.xhtml"),
	PROCESSO_SELETIVO(15, PerfilAcessoPermissaoProcessoSeletivoEnum.values(), "/visaoAdministrativo/processoSeletivo/menuProcessoSeletivo.xhtml"),
	RH(16, PerfilAcessoPermissaoRhEnum.values(), "/visaoAdministrativo/rh/menuRecursosHumanos.xhtml"),
	SEI_DECIDIR(17, PerfilAcessoPermissaoSeiDecidirEnum.values(), ""),
	VISAO_ALUNO(18, PerfilAcessoPermissaoVisaoAlunoEnum.values(), "/visaoAluno/menuVisaoAluno.xhtml"),
	VISAO_PROFESSOR(19, PerfilAcessoPermissaoVisaoProfessorEnum.values(), "/visaoProfessor/menuVisaoProfessor.xhtml"),
	VISAO_PARCEIRO(20, PerfilAcessoPermissaoVisaoParceiroEnum.values(), "/visaoParceiro/menuVisaoParceiro.xhtml"),
	VISAO_COORDENADOR(21, PerfilAcessoPermissaoVisaoCoordenadorEnum.values(), "/visaoCoordenador/menuVisaoCoordenador.xhtml"),
	HOME_CANDIDATO(22, PerfilAcessoPermissaoHomeCandidatoEnum.values(), "");
	
	private Integer ordem;	
	private String caminhoMenu;
	
	/**
	 * @param ordem
	 */
	private PerfilAcessoModuloEnum(Integer ordem, Enum<? extends PerfilAcessoPermissaoEnumInterface>[] perfilAcessoPermissaoEnumInterfaces, String caminhoMenu) {
		this.ordem = ordem;
		this.caminhoMenu = caminhoMenu;
		this.perfilAcessoPermissaoEnumInterfaces = perfilAcessoPermissaoEnumInterfaces;
		if(perfilAcessoPermissaoEnumInterfaces != null){
			for(Enum<? extends PerfilAcessoPermissaoEnumInterface> per: perfilAcessoPermissaoEnumInterfaces){
				((PerfilAcessoPermissaoEnumInterface)per).getPerfilAcessoSubModulo().setPerfilAcessoModuloEnum(this);				
			}
		}
	}
		

	private Enum<? extends PerfilAcessoPermissaoEnumInterface>[] perfilAcessoPermissaoEnumInterfaces;
	
	/*
	 * Transientes
	 */
	private static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidadesVisaoAdministrativa;
	private static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidadesVisaoAluno;
	private static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidadesVisaoPais;
	private static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidadesVisaoCoordenador;
	private static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidadesVisaoProfessor;
	private static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidadesVisaoParceiro;
	private static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidadesVisaoCandidato;
	

	public Enum<? extends PerfilAcessoPermissaoEnumInterface>[] getPerfilAcessoPermissaoEnumInterfaces() {		
		return perfilAcessoPermissaoEnumInterfaces;
	}

	public void setPerfilAcessoPermissaoEnumInterfaces(Enum<? extends PerfilAcessoPermissaoEnumInterface>[] perfilAcessoPermissaoEnumInterfaces) {
		this.perfilAcessoPermissaoEnumInterfaces = perfilAcessoPermissaoEnumInterfaces;
	}
	
	
	public static Enum<? extends PerfilAcessoPermissaoEnumInterface> getEnumPorValor(String valor) {
		for (PerfilAcessoModuloEnum acessoPermissao : PerfilAcessoModuloEnum.values()) {
			if(acessoPermissao.getPerfilAcessoPermissaoEnumInterfaces() == null) {
				continue;
			}
			for (Enum<? extends PerfilAcessoPermissaoEnumInterface> permissao : acessoPermissao.getPerfilAcessoPermissaoEnumInterfaces()) {
				if (((PerfilAcessoPermissaoEnumInterface)permissao).getValor().equals(valor)) {
					return permissao;
				}
			}
		}
		return null;
	}
	
	/**
	 * @return the entidadesVisaoAdministrativa
	 */
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getEntidadesVisaoAdministrativa() {
		if (entidadesVisaoAdministrativa == null) {
			entidadesVisaoAdministrativa = getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum.ADMINISTRATIVA);
		}
		return entidadesVisaoAdministrativa;
	}

	/**
	 * @return the entidadesVisaoAluno
	 */
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getEntidadesVisaoAluno() {
		if (entidadesVisaoAluno == null) {
			entidadesVisaoAluno =  getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum.ALUNO);
		}
		return entidadesVisaoAluno;
	}


	/**
	 * @return the entidadesVisaoPais
	 */
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getEntidadesVisaoPais() {
		if (entidadesVisaoPais == null) {
			entidadesVisaoPais =  getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum.PAIS);
		}
		return entidadesVisaoPais;
	}


	/**
	 * @return the entidadesVisaoCoordenador
	 */
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getEntidadesVisaoCoordenador() {
		if (entidadesVisaoCoordenador == null) {
			entidadesVisaoCoordenador =  getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum.COORDENADOR);
		}
		return entidadesVisaoCoordenador;
	}

	/**
	 * @return the entidadesVisaoProfessor
	 */
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getEntidadesVisaoProfessor() {
		if (entidadesVisaoProfessor == null) {
			entidadesVisaoProfessor =  getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum.PROFESSOR);
		}
		return entidadesVisaoProfessor;
	}

	/**
	 * @return the entidadesVisaoParceiro
	 */
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getEntidadesVisaoParceiro() {
		if (entidadesVisaoParceiro == null) {
			entidadesVisaoParceiro =  getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum.PARCEIRO);
		}
		return entidadesVisaoParceiro;
	}	

	/**
	 * @return the entidadesVisaoCandidato
	 */
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getEntidadesVisaoCandidato() {
		if (entidadesVisaoCandidato == null) {
			entidadesVisaoCandidato = getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum.CANDIDATO);
			
		}
		return entidadesVisaoCandidato;
	}
	
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getListaEntidadePorTipoVisaoSistema(TipoVisaoEnum tipoVisaoSistemaEnum){
		List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> entidades = new ArrayList<Enum<? extends PerfilAcessoPermissaoEnumInterface>>(0);
		for (PerfilAcessoModuloEnum perfilAcessoPermissaoEnum : PerfilAcessoModuloEnum.values()) {
			if(perfilAcessoPermissaoEnum.getPerfilAcessoPermissaoEnumInterfaces() == null) {
				continue;
			}
			for (Enum<? extends PerfilAcessoPermissaoEnumInterface> perfilAcessoEntidadeEnum : perfilAcessoPermissaoEnum.getPerfilAcessoPermissaoEnumInterfaces()) {
				PerfilAcessoPermissaoEnumInterface perfilAcessoPermissaoEnumInterface = (PerfilAcessoPermissaoEnumInterface) perfilAcessoEntidadeEnum;
				if ((perfilAcessoPermissaoEnumInterface.getTipoPerfilAcesso().getIsEntidade() || perfilAcessoPermissaoEnumInterface.getTipoPerfilAcesso().getIsRelatorio()) && perfilAcessoPermissaoEnumInterface.getUtilizaVisao(tipoVisaoSistemaEnum)) {
					entidades.add(perfilAcessoEntidadeEnum);
				}
			}
		}
		return entidades;
	}
	
	public static Enum<? extends PerfilAcessoPermissaoEnumInterface> getEnumsPorValor(String valor){
		for (PerfilAcessoModuloEnum perModulo : PerfilAcessoModuloEnum.values()) {
			if(perModulo.getPerfilAcessoPermissaoEnumInterfaces() == null) {
				continue;
			}
			for (Enum<? extends PerfilAcessoPermissaoEnumInterface> permissao : perModulo.getPerfilAcessoPermissaoEnumInterfaces()) {
				PerfilAcessoPermissaoEnumInterface perfilAcessoPermissaoEnumInterface = (PerfilAcessoPermissaoEnumInterface) permissao;
				if (perfilAcessoPermissaoEnumInterface.getValor().equals(valor)) {
					return permissao;
				}
			}
		}
		return null;
	}	
	
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getFuncionalidadePorValorEntidade(String valor, TipoVisaoEnum tipoVisaoEnum){
		return getFuncionalidadePorPermissaoEntidade(getEnumPorValor(valor), tipoVisaoEnum);
	}
	
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getFuncionalidadePorPermissaoEntidade(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissao, TipoVisaoEnum tipoVisaoEnum) {
		List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> funcionalidades = null;
		if (permissao != null && (((PerfilAcessoPermissaoEnumInterface)permissao).getTipoPerfilAcesso().getIsEntidade() || ((PerfilAcessoPermissaoEnumInterface)permissao).getTipoPerfilAcesso().getIsRelatorio())) {
			funcionalidades = new ArrayList<Enum<? extends PerfilAcessoPermissaoEnumInterface>>(0);
			for (PerfilAcessoModuloEnum perModulo : PerfilAcessoModuloEnum.values()) {
				if(perModulo.getPerfilAcessoPermissaoEnumInterfaces() == null) {
					continue;
				}				
				for (Enum<? extends PerfilAcessoPermissaoEnumInterface> per : perModulo.getPerfilAcessoPermissaoEnumInterfaces()) {
					PerfilAcessoPermissaoEnumInterface perfilAcessoPermissaoEnumInterface = (PerfilAcessoPermissaoEnumInterface) per;
					if (perfilAcessoPermissaoEnumInterface.getTipoPerfilAcesso().getIsFuncionalidade() 
							&& perfilAcessoPermissaoEnumInterface.getPermissaoSuperiorEnum().equals(permissao) 
							&& perfilAcessoPermissaoEnumInterface.getUtilizaVisao(tipoVisaoEnum)) {
						funcionalidades.add(per);
					}
				}
			}
		}
		return funcionalidades;
	}
	
	public static List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> getPermissaoPorTelaAmbiente(String tela, TipoVisaoEnum tipoVisaoEnum) {
		List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> permissoes = new ArrayList<Enum<? extends PerfilAcessoPermissaoEnumInterface>>(0);
		for (PerfilAcessoModuloEnum perModulo : PerfilAcessoModuloEnum.values()) {
			if(perModulo.getPerfilAcessoPermissaoEnumInterfaces() == null) {
				continue;
			}
			for (Enum<? extends PerfilAcessoPermissaoEnumInterface> per : perModulo.getPerfilAcessoPermissaoEnumInterfaces()) {
				PerfilAcessoPermissaoEnumInterface perfilAcessoPermissaoEnumInterface = (PerfilAcessoPermissaoEnumInterface) per;
				if (!perfilAcessoPermissaoEnumInterface.getTipoPerfilAcesso().getIsFuncionalidade() && perfilAcessoPermissaoEnumInterface.getUtilizaVisao(tipoVisaoEnum) && perfilAcessoPermissaoEnumInterface.getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso().length > 0) {
					for (String telaPermissao : perfilAcessoPermissaoEnumInterface.getPermissaoVisao(tipoVisaoEnum).getPaginaAcesso()) {
						if (telaPermissao.trim().equalsIgnoreCase(tela.trim())) {
							permissoes.add(per);
						}
					}
				}
			}
		}
		return permissoes;
	}

	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_PerfilAcessoModuloEnum_"+this.name());
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getCaminhoMenu() {
		if(caminhoMenu == null) {
			caminhoMenu = "";
		}
		return caminhoMenu;
	}

	public void setCaminhoMenu(String caminhoMenu) {
		this.caminhoMenu = caminhoMenu;
	}
	
	
}
