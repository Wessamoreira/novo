package negocio.comuns.arquitetura.enumeradores;

import java.util.Arrays;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoEstagioEnum implements PerfilAcessoPermissaoEnumInterface {
	
	/**
	* Estagio
	*
	*/	
//	ESTAGIO("Estagio",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_Estagio_titulo"), UteisJSF.internacionalizar("per_Estagio_ajuda"),
//					new String[] { "estagioCons.xhtml", "estagioForm.xhtml" }) },
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	ESTAGIO_OBRIGATORIO("EstagioObrigatorio", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_EstagioObrigatorio_titulo"),UteisJSF.internacionalizar("per_EstagioObrigatorio_ajuda"), new String[]{"estagioObrigatorioCons.xhtml","mapaEstagioObrigatorioCons.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	PERMITIR_RESPONDER_CHECKLIST_ESTAGIO_OBRIGATORIO("PermitirResponderChecklistEstagioObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirResponderChecklistEstagioObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_PermitirResponderChecklistEstagioObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO_OBRIGATORIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	PERMITIR_ESTORNAR_INDEFERIMENTO_ESTAGIO_OBRIGATORIO("PermitirEstornarIndeferimentoEstagioObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirEstornarIndeferimentoEstagioObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_PermitirEstornarIndeferimentoEstagioObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO_OBRIGATORIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	PERMITIR_ESTORNAR_DEFERIMENTO_ESTAGIO_OBRIGATORIO("PermitirEstornarDeferimentoEstagioObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirEstornarDeferimentoEstagioObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_PermitirEstornarDeferimentoEstagioObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO_OBRIGATORIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	PERMITIR_ESTORNAR_CORRECAO_ESTAGIO_OBRIGATORIO("PermitirEstornarCorrecaoEstagioObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirEstornarCorrecaoEstagioObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_PermitirEstornarCorrecaoEstagioObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO_OBRIGATORIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	PERMITIR_DEFERIMENTO_ESTAGIO_OBRIGATORIO("PermitirDeferimentoEstagioObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirDeferimentoEstagioObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_PermitirDeferimentoEstagioObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO_OBRIGATORIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	PERMITIR_INDEFERIMENTO_ESTAGIO_OBRIGATORIO("PermitirIndeferimentoEstagioObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirIndeferimentoEstagioObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_PermitirIndeferimentoEstagioObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO_OBRIGATORIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),	
	
	
	PERMITIR_CORRIGIR_FORMULARIO_ESTAGIO_OBRIGATORIO("PermitirCorrigirFormularioEstagioObrigatorio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirCorrigirFormularioEstagioObrigatorio_titulo"),
					UteisJSF.internacionalizar("per_PermitirCorrigirFormularioEstagioObrigatorio_ajuda")) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO_OBRIGATORIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
//	ESTAGIO_LIBERAR_VALIDACAO("Estagio_liberarValidacao",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_EstagioliberarValidacao_titulo"),
//					UteisJSF.internacionalizar("per_EstagioliberarValidacao_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO,
//			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
//	ESTAGIO_PERMITIR_IMPRESSAO_CONTRATO_ESTAGIO_OBRIGATORIO("Estagio_permitirImpressaoContratoEstagioObrigatorio",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_EstagiopermitirImpressaoContratoEstagioObrigatorio_titulo"),
//					UteisJSF.internacionalizar("per_EstagiopermitirImpressaoContratoEstagioObrigatorio_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO,
//			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
//	ESTAGIO_PERMITIR_IMPRESSAO_CONTRATO_ESTAGIO_NAO_OBRIGATORIO(
//			"Estagio_permitirImpressaoContratoEstagioNaoObrigatorio",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_EstagiopermitirImpressaoContratoEstagioNaoObrigatorio_titulo"),
//					UteisJSF.internacionalizar("per_EstagiopermitirImpressaoContratoEstagioNaoObrigatorio_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO,
//			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
//	ESTAGIO_PERMITIR_INFORMAR_SEGURADORA_ESTAGIO("Estagio_permitirInformarSeguradoraEstagio",
//			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
//					UteisJSF.internacionalizar("per_Estagio_permitirInformarSeguradoraEstagio_titulo"),
//					UteisJSF.internacionalizar("per_Estagio_permitirInformarSeguradoraEstagio_ajuda")) },
//			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PerfilAcessoPermissaoEstagioEnum.ESTAGIO,
//			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),		
	
	
	CONFIGURACAO_ESTAGIO_OBRIGATORIO("ConfiguracaoEstagioObrigatorio", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoEstagioObrigatorio_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoEstagioObrigatorio_ajuda"), new String[]{"configuracaoEstagioObrigatorioForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	MOTIVOS_PADROES_ESTAGIO("MotivosPadroesEstagio", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MotivosPadroesEstagio_titulo"),UteisJSF.internacionalizar("per_MotivosPadroesEstagio_ajuda"), new String[]{"motivosPadroesEstagioCons.xhtml","motivosPadroesEstagioForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	CONCEDENTE("Concedente", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Concedente_titulo"),UteisJSF.internacionalizar("per_Concedente_ajuda"), new String[]{"concedenteCons.xhtml","concedenteForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	
	TIPO_CONCEDENTE("TipoConcedente", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_TipoConcedente_titulo"),UteisJSF.internacionalizar("per_TipoConcedente_ajuda"), new String[]{"tipoConcedenteCons.xhtml","tipoConcedenteForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	MODELO_TERMO_ESTAGIO("ModeloTermoEstagio", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ModeloTermoEstagio_titulo"),UteisJSF.internacionalizar("per_ModeloTermoEstagio_ajuda"), new String[]{"modeloTermoEstagioCons.xhtml","modeloTermoEstagioForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	GRUPO_PESSOA("GrupoPessoa", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_GrupoPessoa_titulo"),UteisJSF.internacionalizar("per_GrupoPessoa_ajuda"), new String[]{"grupoPessoaCons.xhtml","grupoPessoaForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	/**
	 * Campos Plano Ensino
	 *
	 */
	CAMPOS_ESTAGIO("CamposEstagio", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CamposEstagio_titulo"),UteisJSF.internacionalizar("per_CamposEstagio_ajuda"),new String[] { "perguntaCons.xhtml", "perguntaForm.xhtml" })
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	/**
	 * Formulario Plano Ensino
	 *
	 */
	FORMULARIO_ESTAGIO("FormularioEstagio", new PermissaoVisao[] {new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FormularioEstagio_titulo"),UteisJSF.internacionalizar("per_FormularioEstagio_ajuda"),new String[] { "questionarioCons.xhtml", "questionarioForm.xhtml" })
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	
	
	/**
	* Estagio Rel
	*
	*/
	ESTAGIO_REL("EstagioRel",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstagioRel_titulo"),
					UteisJSF.internacionalizar("per_EstagioRel_ajuda"), new String[] { "estagioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ESTAGIO_RELATORIO),
	PERMITE_REGISTRAR_ESTAGIO_SEGURADORA("PermiteRegistrarEstagioSeguradora",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_EstagioRel_permitirRegistrarEnvioEstagioSeguradora"),
					UteisJSF.internacionalizar("per_EstagioRel_permitirRegistrarEnvioEstagioSeguradora_ajuda"),
					new String[] { "estagioRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, ESTAGIO_REL, PerfilAcessoSubModuloEnum.ESTAGIO_RELATORIO),

    PAINEL_MONITORAMENTO_ESTAGIO("PainelMonitoramentoEstagio",
            new PermissaoVisao[] {new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_PainelMonitoramentoEstagio_titulo"),
            UteisJSF.internacionalizar("per_PainelMonitoramentoEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"})},
            TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),

    PERMITE_VISUALIZAR_APENAS_ESTAGIO_DE_SUA_RESPONSABILIDADE("PermiteVisualizarApenasEstagiosDeSuaResponsabilidade",
            new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirVisualizarApenasEstagioDeSuaResponsabilidade_titulo"),
            UteisJSF.internacionalizar("per_permitirVisualizarApenasEstagioDeSuaResponsabilidade_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),

    PERMITE_SOLICITAR_ASSINATURA_TERMO_ESTAGIO("PermiteSolicitarAssinaturaTermoEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permiteSolicitarAssinaturaTermoEstagio_titulo"),
            UteisJSF.internacionalizar("per_permiteSolicitarAssinaturaTermoEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_SOLICITAR_ASSINATURA_TERMO_ADITIVO("PermiteSolicitarAssinaturaTermoAditivo", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permiteSolicitarAssinaturaTermoAditivo_titulo"),
            UteisJSF.internacionalizar("per_permiteSolicitarAssinaturaTermoAditivo_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_SOLICITAR_ASSINATURA_TERMO_RESCISAO("PermiteSolicitarAssinaturaTermoRescisao", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permiteSolicitarAssinaturaTermoRescisao_titulo"),
            UteisJSF.internacionalizar("per_permiteSolicitarAssinaturaTermoRescisao_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_DEFERIR_ESTAGIO("PermiteDeferirEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirDeferirEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirDeferirEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_DEFERIR_TERMO_ADITIVO("PermiteDeferirTermoAditivo", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirDeferirTermoAditivo_titulo"),
            UteisJSF.internacionalizar("per_permitirDeferirTermoAditivo_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_DEFERIR_TERMO_RESCISAO("PermiteDeferirTermoRescisao", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirDeferirTermoRescisao_titulo"),
            UteisJSF.internacionalizar("per_permitirDeferirTermoRescisao_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_DEFERIR_APROVEITAMENTO("PermiteDeferirAproveitamento", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirDeferirAproveitamento_titulo"),
            UteisJSF.internacionalizar("per_permitirDeferirAproveitamento_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_DEFERIR_RELATORIO_ESTAGIO("PermiteDeferirRelatorioEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirDeferirRelatorioEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirDeferirRelatorioEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_INDEFERIR_ESTAGIO("PermiteIndeferirEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirIndeferirEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirIndeferirEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_INDEFERIR_TERMO_ADITIVO("PermiteIndeferirTermoAditivo", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirIndeferirTermoAditivo_titulo"),
            UteisJSF.internacionalizar("per_permitirIndeferirTermoAditivo_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_INDEFERIR_TERMO_RESCISAO("PermiteIndeferirTermoRescisao", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirIndeferirTermoRescisao_titulo"),
            UteisJSF.internacionalizar("per_permitirIndeferirTermoRescisao_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_INDEFERIR_APROVEITAMENTO("PermiteIndeferirAproveitamento", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirIndeferirAproveitamento_titulo"),
            UteisJSF.internacionalizar("per_permitirIndeferirAproveitamento_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_INDEFERIR_RELATORIO_ESTAGIO("PermiteIndeferirRelatorioEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirIndeferirRelatorioEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirIndeferirRelatorioEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ANALISAR_TERMO_ESTAGIO("PermiteAnalisarTermoEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirAnalisarTermoEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirAnalisarTermoEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ANALISAR_TERMO_ADITIVO("PermiteAnalisarTermoAditivo", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirAnalisarTermoAditivo_titulo"),
            UteisJSF.internacionalizar("per_permitirAnalisarTermoAditivo_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ANALISAR_TERMO_RESCISAO("PermiteAnalisarTermoRescisao", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirAnalisarTermoRescisao_titulo"),
            UteisJSF.internacionalizar("per_permitirAnalisarTermoRescisao_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ANALISAR_APROVEITAMENTO("PermiteAnalisarAproveitamento", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirAnalisarAproveitamento_titulo"),
            UteisJSF.internacionalizar("per_permitirAnalisarAproveitamento_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ANALISAR_RELATORIO_ESTAGIO("PermiteAnalisarRelatorioEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirAnalisarRelatorioEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirAnalisarRelatorioEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_DEFERIMENTO_ESTAGIO("PermitirEstornarDeferimentoEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_DEFERIMENTO_TERMO_ADITIVO("PermitirEstornarDeferimentoTermoAditivo", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoTermoAditivo_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoTermoAditivo_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_DEFERIMENTO_TERMO_RESCISAO("PermitirEstornarDeferimentoTermoRescisao", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoTermoRescisao_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoTermoRescisao_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_DEFERIMENTO_APROVEITAMENTO("PermitirEstornarDeferimentoAproveitamento", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoAproveitamento_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoAproveitamento_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_DEFERIMENTO_RELATORIO_ESTAGIO("PermitirEstornarDeferimentoRelatorioEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoRelatorioEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarDeferimentoRelatorioEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_INDEFERIMENTO_ESTAGIO("PermitirEstornarIndeferimentoEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_INDEFERIMENTO_TERMO_ADITIVO("PermitirEstornarIndeferimentoTermoAditivo", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoTermoAditivo_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoTermoAditivo_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_INDEFERIMENTO_TERMO_RESCISAO("PermitirEstornarIndeferimentoTermoRescisao", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoTermoRescisao_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoTermoRescisao_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_INDEFERIMENTO_APROVEITAMENTO("PermitirEstornarIndeferimentoAproveitamento", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoAproveitamento_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoAproveitamento_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
    PERMITE_ESTORNAR_INDEFERIMENTO_RELATORIO_ESTAGIO("PermitirEstornarIndeferimentoRelatorioEstagio", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoRelatorioEstagio_titulo"),
            UteisJSF.internacionalizar("per_permitirEstornarIndeferimentoRelatorioEstagio_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
            TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	PERMITE_REALIZAR_SUBSTITUICAO_AVALIADOR("PermitirRealizarSubstituicaoAvaliador", new PermissaoVisao[]{ new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
			UteisJSF.internacionalizar("per_permitirRealizarSubstituicaoAvaliador_titulo"),
			UteisJSF.internacionalizar("per_permitirRealizarSubstituicaoAvaliador_ajuda"), new String[] {"painelMonitoramentoEstagioCons.xhtml"}) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, PAINEL_MONITORAMENTO_ESTAGIO, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),
	LOG_ESTAGIO("LogEstagio",
			new PermissaoVisao[] {new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_LogEstagio_titulo"),
					UteisJSF.internacionalizar("per_LogEstagio_ajuda"), new String[] {"logEstagio.xhtml"})},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ESTAGIO_ESTAGIO),

	
	/**
	 * Relatorio SEI Decidir Estagio
	 *
	 */
	RELATORIO_SEIDECIDIR_ESTAGIO("RelatorioSEIDecidirEstagio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirEstagio_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirEstagio_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ESTAGIO_RELATORIO),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_ESTAGIO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirEstagioApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirEstagioApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirEstagioApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_ESTAGIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_RELATORIO),
	
	PERMITIR_VISUALIZAR_SCRIPT_SQL_RELATORIO_SEIDECIDIR_ESTAGIO("PermitirVisualizarScriptSqlRelatorioSeiDecidirEstagio",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_ESTAGIO,
			PerfilAcessoSubModuloEnum.ESTAGIO_RELATORIO);

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for
	 *        funcionalidade
	 */

	private PerfilAcessoPermissaoEstagioEnum(String valor, PermissaoVisao[] permissaoVisao, 
			TipoPerfilAcessoPermissaoEnum tipoPerfilAcesso,
			Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoSuperiorEnum,
			PerfilAcessoSubModuloEnum perfilAcessoSubModulo) {
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
		return getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE)
				&& getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO);
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
				descricaoModuloSubModulo = getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getValorApresentar()
						+ " - " + getPerfilAcessoSubModulo().getDescricao();
		}else {
			descricaoModuloSubModulo ="";
		}
		}
		return descricaoModuloSubModulo;
	}		 
		
}
