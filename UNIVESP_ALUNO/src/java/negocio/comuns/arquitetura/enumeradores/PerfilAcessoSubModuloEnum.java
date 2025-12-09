/**
 * 
 */
package negocio.comuns.arquitetura.enumeradores;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoSubModuloEnum {

	//Sub-Modulo Financeiro
	FINANCEIRO_BANCO_PARCEIRO(UteisJSF.internacionalizar("per_FINANCEIRO_BANCO_PARCEIRO_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 1),
	FINANCEIRO_CONTA_PAGAR(UteisJSF.internacionalizar("per_FINANCEIRO_CONTA_PAGAR_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 2),
	FINANCEIRO_CONTA_RECEBER(UteisJSF.internacionalizar("per_FINANCEIRO_CONTA_RECEBER_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 3),
	FINANCEIRO_PRESTACAO_CONTA(UteisJSF.internacionalizar("per_FINANCEIRO_PRESTACAO_CONTA_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 4),
	FINANCEIRO_LIBERACAO_FINANCEIRO(UteisJSF.internacionalizar("per_FINANCEIRO_LIBERACAO_FINANCEIRO_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 5),
	FINANCEIRO_CAIXA(UteisJSF.internacionalizar("per_FINANCEIRO_CAIXA_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 6),
	FINANCEIRO_FATURAMENTO(UteisJSF.internacionalizar("per_FINANCEIRO_FATURAMENTO_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 7),
	FINANCEIRO_FINANCEIRO_ACADEMICO(UteisJSF.internacionalizar("per_FINANCEIRO_FINANCEIRO_ACADEMICO_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 8),
	FINANCEIRO_PERFIL_ECONOMICO(UteisJSF.internacionalizar("per_FINANCEIRO_PERFIL_ECONOMICO_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 9),
	FINANCEIRO_RELATORIOS_CONTA_RECEBER(UteisJSF.internacionalizar("per_FINANCEIRO_RELATORIOS_CONTA_RECEBER_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 10),
	FINANCEIRO_RELATORIOS_CONTA_PAGAR(UteisJSF.internacionalizar("per_FINANCEIRO_RELATORIOS_CONTA_PAGAR_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 11),
	FINANCEIRO_RELATORIOS_DESCONTOS(UteisJSF.internacionalizar("per_FINANCEIRO_RELATORIOS_DESCONTOS_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 12),
	FINANCEIRO_RELATORIOS_CAIXA(UteisJSF.internacionalizar("per_FINANCEIRO_RELATORIOS_CAIXA_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 13),
	FINANCEIRO_SEI_DECIDIR_FINANCEIRO(UteisJSF.internacionalizar("per_FINANCEIRO_SEI_DECIDIR_FINANCEIRO_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 14),
	FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER(UteisJSF.internacionalizar("per_FINANCEIRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER_titulo"), PerfilAcessoModuloEnum.FINANCEIRO, 15),
	
	// Sub-Modulo Administrativo
	ADMINISTRATIVO_ADMINISTRATIVO(UteisJSF.internacionalizar("per_ADMINISTRATIVO_ADMINISTRATIVO_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 1),
	ADMINISTRATIVO_ATENDIMENTO_ALUNO(UteisJSF.internacionalizar("per_ADMINISTRATIVO_ATENDIMENTO_ALUNO_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 2),
	ADMINISTRATIVO_BASICO(UteisJSF.internacionalizar("per_ADMINISTRATIVO_BASICO_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 3),
	ADMINISTRATIVO_CONTABIL(UteisJSF.internacionalizar("per_ADMINISTRATIVO_CONTABIL_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 4),
	ADMINISTRATIVO_CONTROLE_ACESSO(UteisJSF.internacionalizar("per_ADMINISTRATIVO_CONTROLE_ACESSO_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 5),
	ADMINISTRATIVO_CONTROLE_PATRIMONIO(UteisJSF.internacionalizar("per_ADMINISTRATIVO_CONTROLE_PATRIMONIO_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 6),
	ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO(UteisJSF.internacionalizar("per_ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 7),
	ADMINISTRATIVO_REQUISICOES(UteisJSF.internacionalizar("per_ADMINISTRATIVO_REQUISICOES_titulo"), PerfilAcessoModuloEnum.ADMINISTRATIVO, 8),

	//Sub-Modulo Acadêmico
	ACADEMICO_ALUNO(UteisJSF.internacionalizar("per_ACADEMICO_ALUNO_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 1),
	ACADEMICO_CURSOS(UteisJSF.internacionalizar("per_ACADEMICO_CURSOS_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 2),
	ACADEMICO_ESTAGIO(UteisJSF.internacionalizar("per_ACADEMICO_ESTAGIO_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 3),
	ACADEMICO_MATRICULAS(UteisJSF.internacionalizar("per_ACADEMICO_MATRICULAS_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 4),
	ACADEMICO_MONOGRAFIA(UteisJSF.internacionalizar("per_ACADEMICO_MONOGRAFIA_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 5),
	ACADEMICO_FORMATURA(UteisJSF.internacionalizar("per_ACADEMICO_FORMATURA_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 6),
	ACADEMICO_SECRETARIA(UteisJSF.internacionalizar("per_ACADEMICO_SECRETARIA_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 7),
	ACADEMICO_TRANSFERENCIAS_TRANCAMENTO(UteisJSF.internacionalizar("per_ACADEMICO_TRANSFERENCIAS_TRANCAMENTO_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 8),
	ACADEMICO_TURMA(UteisJSF.internacionalizar("per_ACADEMICO_TURMA_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 9),
	ACADEMICO_RELATORIOS_ACADEMICO(UteisJSF.internacionalizar("per_ACADEMICO_RELATORIOS_ACADEMICO_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 10),
	ACADEMICO_RELATORIOS_CRONOGRAMA(UteisJSF.internacionalizar("per_ACADEMICO_RELATORIOS_CRONOGRAMA_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 11),
	ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES(UteisJSF.internacionalizar("per_ACADEMICO_RELATORIOS_CERTIFICADOS_DECLARACOES_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 12),
	ACADEMICO_RELATORIOS_DADOS_ALUNOS(UteisJSF.internacionalizar("per_ACADEMICO_RELATORIOS_DADOS_ALUNOS_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 13),
	ACADEMICO_RELATORIOS_DADOS_PROFESSORES(UteisJSF.internacionalizar("per_ACADEMICO_RELATORIOS_DADOS_PROFESSORES_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 14),
	ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS(UteisJSF.internacionalizar("per_ACADEMICO_RELATORIOS_DADOS_ESTATISTICOS_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 15),
	ACADEMICO_GERACAO_ARQUIVOS(UteisJSF.internacionalizar("per_ACADEMICO_GERACAO_ARQUIVOS_titulo"), PerfilAcessoModuloEnum.ACADEMICO, 16),
	ACADEMICO_SEI_DECIDIR_ACADEMICO(UteisJSF.internacionalizar("per_ACADEMICO_SEI_DECIDIR_ACADEMICO_titulo"), PerfilAcessoModuloEnum.ACADEMICO,17),
	ACADEMICO_SEI_DECIDIR_REQUERIMENTO(UteisJSF.internacionalizar("per_ACADEMICO_SEI_DECIDIR_REQUERIMENTO_titulo"), PerfilAcessoModuloEnum.ACADEMICO,18),
	
	//Sub-Modulo Compras
	COMPRAS_BASICO(UteisJSF.internacionalizar("per_COMPRAS_BASICO_titulo"), PerfilAcessoModuloEnum.COMPRAS, 1),
	COMPRAS_REQUISICOES(UteisJSF.internacionalizar("per_COMPRAS_REQUISICOES_titulo"), PerfilAcessoModuloEnum.COMPRAS, 2),
	COMPRAS_COTACAO_COMPRA(UteisJSF.internacionalizar("per_COMPRAS_COTACAO_COMPRA_titulo"), PerfilAcessoModuloEnum.COMPRAS, 3),
	COMPRAS_RELATORIOS(UteisJSF.internacionalizar("per_COMPRAS_RELATORIOS_titulo"), PerfilAcessoModuloEnum.COMPRAS, 4),
	
	//Sub-Modulo Biblioteca
	BIBLIOTECA_BIBLIOTECA(UteisJSF.internacionalizar("per_BIBLIOTECA_BIBLIOTECA_titulo"), PerfilAcessoModuloEnum.BIBLIOTECA, 1),
	BIBLIOTECA_CONSULTA_ACERVO(UteisJSF.internacionalizar("per_BIBLIOTECA_CONSULTA_ACERVO_titulo"), PerfilAcessoModuloEnum.BIBLIOTECA, 2),
	BIBLIOTECA_EMPRESTIMO_DEVOLUCAO(UteisJSF.internacionalizar("per_BIBLIOTECA_EMPRESTIMO_DEVOLUCAO_titulo"), PerfilAcessoModuloEnum.BIBLIOTECA, 3),
	BIBLIOTECA_RELATORIOS_BIBLIOTECA(UteisJSF.internacionalizar("per_BIBLIOTECA_RELATORIOS_BIBLIOTECA_titulo"), PerfilAcessoModuloEnum.BIBLIOTECA, 4),
	BIBLIOTECA_BLOQUEIO_BIBLIOTECA(UteisJSF.internacionalizar("per_BIBLIOTECA_BLOQUEIO_BIBLIOTECA_titulo"), PerfilAcessoModuloEnum.BIBLIOTECA, 5),
	BIBLIOTECA_OPCOES(UteisJSF.internacionalizar("per_BIBLIOTECA_OPCOES_titulo"), PerfilAcessoModuloEnum.BIBLIOTECA, 6),
	
	//Sub-Modulo Processo Seletivo
	PROCESSO_SELETIVO(UteisJSF.internacionalizar("per_PROCESSO_SELETIVO_titulo"), PerfilAcessoModuloEnum.PROCESSO_SELETIVO, 1),
	PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO(UteisJSF.internacionalizar("per_PROCESSO_SELETIVO_PROVA_PROCESSO_SELETIVO_titulo"), PerfilAcessoModuloEnum.PROCESSO_SELETIVO, 2),
	PROCESSO_SELETIVO_QUESTIONARIO_PROCESSO_SELETIVO(UteisJSF.internacionalizar("per_PROCESSO_SELETIVO_QUESTIONARIO_PROCESSO_SELETIVO_titulo"), PerfilAcessoModuloEnum.PROCESSO_SELETIVO, 3),
	PROCESSO_SELETIVO_RELATORIO(UteisJSF.internacionalizar("per_PROCESSO_SELETIVO_RELATORIO_titulo"), PerfilAcessoModuloEnum.PROCESSO_SELETIVO, 4),
	
	//Sub-Modulo Avaliação Institucional
	AVALIACAO_INSTITUCIONAL_AVALIACAO_INSTITUCIONAL(UteisJSF.internacionalizar("per_AVALIACAO_INSTITUCIONAL_AVALIACAO_INSTITUCIONAL_titulo"), PerfilAcessoModuloEnum.AVALIACAO_INSTITUCIONAL, 1),
	AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL(UteisJSF.internacionalizar("per_AVALIACAO_INSTITUCIONAL_RELATORIOS_AVALIACAO_INSTITUCIONAL_titulo"), PerfilAcessoModuloEnum.AVALIACAO_INSTITUCIONAL, 2),
	
	//Sub-Modulo Banco de Curriculos
//	BANCO_DE_CURRICULOS_BASICO(UteisJSF.internacionalizar("per_BANCO_DE_CURRICULOS_BASICO_titulo"), PerfilAcessoModuloEnum.BANCO_DE_CURRICULOS, 1),
//	BANCO_DE_CURRICULOS_RELATORIO(UteisJSF.internacionalizar("per_BANCO_DE_CURRICULOS_RELATORIO_titulo"), PerfilAcessoModuloEnum.BANCO_DE_CURRICULOS, 2),

	//Sub-Modulo EAD
	EAD_EAD(UteisJSF.internacionalizar("per_EAD_EAD_titulo"), PerfilAcessoModuloEnum.EAD, 1),
	EAD_BANCO_QUESTOES(UteisJSF.internacionalizar("per_EAD_BANCO_QUESTOES_titulo"), PerfilAcessoModuloEnum.EAD, 2),
	EAD_CONFIGURACOES(UteisJSF.internacionalizar("per_EAD_CONFIGURACOES_titulo"), PerfilAcessoModuloEnum.EAD, 3),
	EAD_RELATORIO(UteisJSF.internacionalizar("per_EAD_RELATORIO_titulo"), PerfilAcessoModuloEnum.EAD, 4),

	//Sub-Modulo CRM
	CRM_CRM(UteisJSF.internacionalizar("per_CRM_CRM_titulo"), PerfilAcessoModuloEnum.CRM, 1),
	CRM_RELATORIOS_CRM(UteisJSF.internacionalizar("per_CRM_RELATORIOS_CRM_titulo"), PerfilAcessoModuloEnum.CRM, 2),
	
	//Sub-Modulo Nota Fiscal
	NOTA_FISCAL_NOTA_FISCAL(UteisJSF.internacionalizar("per_NOTA_FISCAL_NOTA_SAIDA_titulo"), PerfilAcessoModuloEnum.NOTA_FISCAL, 1),
	NOTA_FISCAL_ENTRADA(UteisJSF.internacionalizar("per_NOTA_FISCAL_NOTA_ENTRADA_titulo"), PerfilAcessoModuloEnum.NOTA_FISCAL, 2),
	NOTA_FISCAL_BASICO(UteisJSF.internacionalizar("per_NOTA_FISCAL_BASICO_titulo"), PerfilAcessoModuloEnum.NOTA_FISCAL, 3),
	NOTA_FISCAL_RELATORIOS_ENTRADA(UteisJSF.internacionalizar("per_NOTA_FISCAL_RELATORIOS_ENTRADA_titulo"), PerfilAcessoModuloEnum.NOTA_FISCAL, 4),
	NOTA_FISCAL_GINFES(UteisJSF.internacionalizar("per_NOTA_FISCAL_GINFES_titulo"), PerfilAcessoModuloEnum.NOTA_FISCAL, 5),
	
	//Sub-Modulo Estagio
	ESTAGIO_ESTAGIO(UteisJSF.internacionalizar("per_ESTAGIO_ESTAGIO_titulo"), PerfilAcessoModuloEnum.ESTAGIO, 1),
	ESTAGIO_RELATORIO(UteisJSF.internacionalizar("per_ESTAGIO_RELATORIO_titulo"), PerfilAcessoModuloEnum.ESTAGIO, 2),

	//Sub-Modulo Patrimonio
	PATRIMONIO_PATRIMONIO(UteisJSF.internacionalizar("per_PATRIMONIO_PATRIMONIO_titulo"), PerfilAcessoModuloEnum.PATRIMONIO, 1),
	PATRIMONIO_RELATORIO(UteisJSF.internacionalizar("per_PATRIMONIO_RELATORIO_titulo"), PerfilAcessoModuloEnum.PATRIMONIO, 2),

	//Sub-Modulo RH
	RH_FOLHA_PAGAMENTO(UteisJSF.internacionalizar("per_RH_FOLHAPAGAMENTO_titulo"), PerfilAcessoModuloEnum.RH, 1),
	RH_BASICO(UteisJSF.internacionalizar("per_RH_BASICO_titulo"), PerfilAcessoModuloEnum.RH, 2),
	RH_EMPRESTIMO(UteisJSF.internacionalizar("per_RH_EMPRESTIMO_titulo"), PerfilAcessoModuloEnum.RH, 3),
	RH_EVENTOS_FUNCIONARIO(UteisJSF.internacionalizar("per_RH_EVENTOS_FUNCIONARIO_titulo"), PerfilAcessoModuloEnum.RH, 4),
	RH_RECURSOS_HUMANOS(UteisJSF.internacionalizar("per_RH_RECURSOS_HUMANOS_titulo"), PerfilAcessoModuloEnum.RH, 5),
	RH_FERIAS(UteisJSF.internacionalizar("per_RH_FERIAS_titulo"), PerfilAcessoModuloEnum.RH, 6),
	RH_RELATORIOS(UteisJSF.internacionalizar("per_RH_RELATORIOS_titulo"), PerfilAcessoModuloEnum.RH, 7),
	RH_FICHA_FUNCIONARIO(UteisJSF.internacionalizar("per_RH_FICHA_FUNCIONARIO_titulo"), PerfilAcessoModuloEnum.RH, 8),
	RH_RESCISAO(UteisJSF.internacionalizar("per_RH_RESCISAO_titulo"), PerfilAcessoModuloEnum.RH, 9),

	//FIM Sub-Modulo RH

	PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO(UteisJSF.internacionalizar("per_PLANO_ORCAMENTARIO_PLANO_ORCAMENTARIO_titulo"), PerfilAcessoModuloEnum.PLANO_ORCAMENTARIO, 1),
	PLANO_ORCAMENTARIO_RELATORIOS(UteisJSF.internacionalizar("per_RH_RELATORIOS_titulo"), PerfilAcessoModuloEnum.PLANO_ORCAMENTARIO, 2),

	TODOS("Todos", PerfilAcessoModuloEnum.TODOS, 0),
	VISAO_PROFESSOR(UteisJSF.internacionalizar("per_VISAO_PROFESSOR_titulo"), PerfilAcessoModuloEnum.VISAO_PROFESSOR, 1),
	SEI_DECIDIR(UteisJSF.internacionalizar("per_SEI_DECIDIR_titulo"), PerfilAcessoModuloEnum.SEI_DECIDIR, 1),
	CONTABIL(UteisJSF.internacionalizar("per_CONTABIL_titulo"), PerfilAcessoModuloEnum.CONTABIL, 1),
	VISAO_COORDENADOR(UteisJSF.internacionalizar("per_VISAO_COORDENADOR_titulo"), PerfilAcessoModuloEnum.VISAO_COORDENADOR, 1),
	VISAO_ALUNO(UteisJSF.internacionalizar("per_VISAO_ALUNO_titulo"), PerfilAcessoModuloEnum.VISAO_ALUNO, 1),
	VISAO_PARCEIRO(UteisJSF.internacionalizar("per_VISAO_PARCEIRO_titulo"), PerfilAcessoModuloEnum.VISAO_PARCEIRO, 1),
	HOME_CANDIDATO(UteisJSF.internacionalizar("per_HOME_CANDIDATO_titulo"), PerfilAcessoModuloEnum.HOME_CANDIDATO, 1);
	
	/**
	 * @param descricao
	 * @param perfilAcessoModuloEnum
	 */
	private PerfilAcessoSubModuloEnum(String descricao, PerfilAcessoModuloEnum perfilAcessoModuloEnum, Integer ordem) {
		this.descricao = descricao;
		this.perfilAcessoModuloEnum = perfilAcessoModuloEnum;
		this.ordem = ordem;
	}
	
	private String descricao;
	private Integer ordem;
	private PerfilAcessoModuloEnum perfilAcessoModuloEnum;
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	/**
	 * @return the perfilAcessoModuloEnum
	 */
	public PerfilAcessoModuloEnum getPerfilAcessoModuloEnum() {
		return perfilAcessoModuloEnum;
	}
	/**
	 * @param perfilAcessoModuloEnum the perfilAcessoModuloEnum to set
	 */
	public void setPerfilAcessoModuloEnum(PerfilAcessoModuloEnum perfilAcessoModuloEnum) {
		this.perfilAcessoModuloEnum = perfilAcessoModuloEnum;
	}
	
	
	public static List<PerfilAcessoSubModuloEnum> getListaPerfilAcessoSubModuloEnumPorModulo(PerfilAcessoModuloEnum modulo){
		List<PerfilAcessoSubModuloEnum> perfilAcessoSubModuloEnums = new ArrayList<PerfilAcessoSubModuloEnum>(0);
		for(PerfilAcessoSubModuloEnum subModulo: PerfilAcessoSubModuloEnum.values()){
			if(subModulo.getPerfilAcessoModuloEnum() != null && subModulo.getPerfilAcessoModuloEnum().equals(modulo)){
				perfilAcessoSubModuloEnums.add(subModulo);
			}
		}
		perfilAcessoSubModuloEnums.add(PerfilAcessoSubModuloEnum.TODOS);
		Ordenacao.ordenarLista(perfilAcessoSubModuloEnums, "ordem");
		return perfilAcessoSubModuloEnums;
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
	
	
	
}
