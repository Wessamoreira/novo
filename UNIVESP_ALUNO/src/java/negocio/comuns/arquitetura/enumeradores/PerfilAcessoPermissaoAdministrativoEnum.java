package negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rodrigo Wind
 *
 */
public enum PerfilAcessoPermissaoAdministrativoEnum implements PerfilAcessoPermissaoEnumInterface {
	
	/**
	 * Comunicacao Interna
	 *
	 */
	COMUNICACAO_INTERNA("ComunicacaoInterna", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInterna_tituloVisaoProfessor"),UteisJSF.internacionalizar("per_ComunicacaoInterna_ajuda"), new String[]{"recadosProfessor.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInterna_tituloVisaoCoordenador"),UteisJSF.internacionalizar("per_ComunicacaoInterna_ajuda"), new String[]{"recadosCoordenador.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInterna_tituloVisaoAluno"),UteisJSF.internacionalizar("per_ComunicacaoInterna_ajuda"), new String[]{"recadoAluno.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_ComunicacaoInterna_tituloPais"),UteisJSF.internacionalizar("per_ComunicacaoInterna_ajuda"), new String[]{"recadoAluno.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInterna_tituloVisaoAdministrativo"),UteisJSF.internacionalizar("per_ComunicacaoInterna_ajuda"), new String[]{"comunicacaoInternaCons.xhtml","comunicacaoInternaForm.xhtml"}
			 )
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TODOS_FUNCIONARIOS("ComunicacaoInterna_enviarParaTodosFuncionarios", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosFuncionarios_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosFuncionarios_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TODOS_ALUNOS("ComunicacaoInterna_enviarParaTodosAlunos", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosAlunos_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosAlunos_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_PERMITE_ANEXAR_ARQUIVO("ComunicacaoInterna_permiteAnexarArquivo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteAnexarArquivo_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TODOS_ALUNOS_ATIVOS("ComunicacaoInterna_enviarParaTodosAlunosAtivos", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosAlunosAtivos_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosAlunosAtivos_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_TIPO_EXIGE_RESPOSTA("ComunicacaoInterna_tipoExigeResposta", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoExigeResposta_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_FUNCIONARIO("ComunicacaoInterna_enviarParaFuncionario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaFuncionario_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaFuncionario_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaFuncionario_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaFuncionario_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TURMA("ComunicacaoInterna_enviarParaTurma", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurma_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurma_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurma_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurma_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurma_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurma_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_PROFESSOR("ComunicacaoInterna_enviarParaProfessor", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaProfessor_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TODA_COMUNIDADE("ComunicacaoInterna_enviarParaTodaComunidade", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodaComunidade_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodaComunidade_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	PERMITIR_COMUNICACAO_INTERNA_ENVIAR_COPIA_POR_EMAIL("PermitirComunicacaoInternaEnviarCopiaPorEmail", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermitirComunicacaoInternaEnviarCopiaPorEmail_titulo"),UteisJSF.internacionalizar("per_PermitirComunicacaoInternaEnviarCopiaPorEmail_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PermitirComunicacaoInternaEnviarCopiaPorEmail_titulo"),UteisJSF.internacionalizar("per_PermitirComunicacaoInternaEnviarCopiaPorEmail_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirComunicacaoInternaEnviarCopiaPorEmail_titulo"),UteisJSF.internacionalizar("per_PermitirComunicacaoInternaEnviarCopiaPorEmail_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TODOS_PROFESSORES("ComunicacaoInterna_enviarParaTodosProfessores", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosProfessores_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosProfessores_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_CARGO("ComunicacaoInterna_enviarParaCargo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCargo_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCargo_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_AREA("ComunicacaoInterna_enviarParaArea", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaArea_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaArea_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_TIPO_SOMENTE_LEITURA("ComunicacaoInterna_tipoSomenteLeitura", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoSomenteLeitura_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_RESPONSAVEL_LEGAL("ComunicacaoInterna_enviarParaResponsavelLegal", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaResponsavelLegal_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaResponsavelLegal_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaResponsavelLegal_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaResponsavelLegal_ajuda"), new String[]{"recadosProfessor.xhtml"}),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_PERMITE_ENVIAR_RESPONSAVEL_FINANCEIRO("ComunicacaoInterna_permiteEnviarResponsavelFinanceiro", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarResponsavelFinanceiro_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarResponsavelFinanceiro_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarResponsavelFinanceiro_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarResponsavelFinanceiro_ajuda"), new String[]{"recadosProfessor.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarResponsavelFinanceiro_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarResponsavelFinanceiro_ajuda"), new String[]{"recadosCoordenador.xhtml"}),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_TIPO_MURAL("ComunicacaoInterna_tipoMural", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternatipoMural_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoMural_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternatipoMural_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoMural_ajuda"), new String[]{"recadosProfessor.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternatipoMural_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternatipoMural_ajuda"), new String[]{"recadosCoordenador.xhtml"}),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TODOS_COORDENADORES("ComunicacaoInterna_enviarParaTodosCoordenadores", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosCoordenadores_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTodosCoordenadores_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_PERMITE_ENVIAR_PAIS("ComunicacaoInterna_permiteEnviarPais", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarPais_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarPais_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarPais_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarPais_ajuda"), new String[]{"recadosProfessor.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarPais_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternapermiteEnviarPais_ajuda"), new String[]{"recadosCoordenador.xhtml"}),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_ALUNO("ComunicacaoInterna_enviarParaAluno", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaAluno_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaAluno_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaAluno_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaAluno_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaAluno_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaAluno_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_COORDENADOR("ComunicacaoInterna_enviarParaCoordenador", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaCoordenador_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_DEPARTAMENTO("ComunicacaoInterna_enviarParaDepartamento", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaDepartamento_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_PERMITIR_RESPONDER_COMUNICADO_SOMENTE_LEITURA_ENTRE_ALUNO_PROFESSOR_COORDENADOR("ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura_ajuda")),			 
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_MEUS_AMIGOS("ComunicacaoInterna_enviarParaMeusAmigos", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_ComunicacaoInterna_enviarParaMeusAmigos_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInterna_enviarParaMeusAmigos_ajuda")),
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_ENVIAR_PARA_TURMA_PERIODO_ANTERIOR("ComunicacaoInterna_enviarParaTurmaPeriodoAnterior", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurmaPeriodoAnterior_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurmaPeriodoAnterior_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurmaPeriodoAnterior_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurmaPeriodoAnterior_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurmaPeriodoAnterior_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaenviarParaTurmaPeriodoAnterior_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	COMUNICACAO_INTERNA_IMPORTAR_PLANILHA_DESTINATARIO("ComunicacaoInterna_importarPlanilhaDestinatario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaImportarPlanilhaDestinatario_titulo"),UteisJSF.internacionalizar("per_PermitirFechamentoNotaBlackBoard_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.COMUNICACAO_INTERNA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	/**
	* Comunicacao Interna Rel
	*
	*/
	COMUNICACAO_INTERNA_REL("ComunicacaoInternaRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ComunicacaoInternaRel_titulo"),UteisJSF.internacionalizar("per_ComunicacaoInternaRel_ajuda"), new String[]{"comunicacaoInternaRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO),
	/**
	* Senha Aluno Professor Rel
	*
	*/
	SENHA_ALUNO_PROFESSOR_REL("SenhaAlunoProfessorRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_SenhaAlunoProfessorRel_titulo"),UteisJSF.internacionalizar("per_SenhaAlunoProfessorRel_ajuda"), new String[]{"senhaAlunoProfessorRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO),
	/**
	* Alunos Ativos Rel
	*
	*/
	ALUNOS_ATIVOS_REL("AlunosAtivosRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AlunosAtivosRel_titulo"),UteisJSF.internacionalizar("per_AlunosAtivosRel_ajuda"), new String[]{"alunosAtivosRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO),
			
	/**
	* Alunos Ativos Rel
	*
	*/
	CONTROLE_ACESSO_ALUNO_CATRACA_REL("ControleAcessoAlunoCatracaRel", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ControleAcessoAlunoCatracaRel_titulo"),UteisJSF.internacionalizar("per_ControleAcessoAlunoCatracaRel_ajuda"), new String[]{"acessoCatracaRel.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.RELATORIO, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO),
	/**
	* Endereco
	*
	*/
	ENDERECO("Endereco", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Endereco_titulo"),UteisJSF.internacionalizar("per_Endereco_ajuda"), new String[]{""})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Cidade
	*
	*/
	CIDADE("Cidade", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Cidade_titulo"),UteisJSF.internacionalizar("per_Cidade_ajuda"), new String[]{"cidadeCons.xhtml","cidadeForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	
	/**
 	 * Links Uteis
 	 * 
 	 */
 	LINKS_UTEIS("LinksUteis", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LinksUteis_titulo"),UteisJSF.internacionalizar("per_LinksUteis_ajuda"), new String[]{"linksUteisCons.xhtml","linksUteisForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
 	
	/**
	* Feriado
	*
	*/
	FERIADO("Feriado", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Feriado_titulo"),UteisJSF.internacionalizar("per_Feriado_ajuda"), new String[]{"feriadoCons.xhtml","feriadoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	PERMITE_EXCLUIR_AULA_PROGRAMADA_GRAVAR_FERIADO("PermiteExcluirAulaProgramadaGravarFeriado", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PermiteExcluirAulaProgramadaGravarFeriado_titulo"),UteisJSF.internacionalizar("per_PermiteExcluirAulaProgramadaGravarFeriado_ajuda"), new String[]{"feriadoCons.xhtml","feriadoForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			FERIADO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Estado
	*
	*/
	ESTADO("Estado", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Estado_titulo"),UteisJSF.internacionalizar("per_Estado_ajuda"), new String[]{"estadoCons.xhtml","estadoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Configuracao TCC
	*
	*/
	CONFIGURACAO_TCC("ConfiguracaoTCC", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoTCC_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoTCC_ajuda"), new String[]{"configuracaoTCCCons.xhtml","configuracaoTCCForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Configuração GED
	*
	*/
	CONFIGURACAO_GED("ConfiguracaoGED", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoGED_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoGED_ajuda"), new String[]{"configuracaoGEDCons.xhtml","configuracaoGEDForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Configuracoes
	*
	*/
	CONFIGURACOES("Configuracoes", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Configuracoes_titulo"),UteisJSF.internacionalizar("per_Configuracoes_ajuda"), new String[]{"configuracoesCons.xhtml","configuracoesForm.xhtml", "configuracaoGeralSistemaForm.xhtml", "configuracaoAcademicoForm.xhtml", "configuracaoFinanceiroForm.xhtml", "cfgCustoAdministrativoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	LIBERAR_ALTERACAO_CONTA_CORRENTE_CARTAO_CREDITO("LiberarAlteracaoContaCorrenteCartaoCredito", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LiberarAlteracaoContaCorrenteCartaoCredito_titulo"),UteisJSF.internacionalizar("per_LiberarAlteracaoContaCorrenteCartaoCredito_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.CONFIGURACOES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	LIBERAR_ALTERACAO_TAXA_OPERACAO_CARTAO_CREDITO("LiberarAlteracaoTaxaOperacaoCartaoCredito", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LiberarAlteracaoTaxaOperacaoCartaoCredito_titulo"),UteisJSF.internacionalizar("per_LiberarAlteracaoTaxaOperacaoCartaoCredito_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.CONFIGURACOES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Tipo Documento
	*
	*/
	TIPO_DOCUMENTO("TipoDocumento", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_TipoDocumento_titulo"),UteisJSF.internacionalizar("per_TipoDocumento_ajuda"), new String[]{"tipoDocumentoCons.xhtml","tipoDocumentoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
			
			/**
	 * CategoriaGED
	 */
	CATEGORIA_GED("CategoriaGED", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_CategoriaGED_titulo"),UteisJSF.internacionalizar("per_CategoriaGED_ajuda"), new String[]{"categoriaGEDCons.xhtml","categoriaGEDForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Biometria
	*
	*/
//	BIOMETRIA("Biometria", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Biometria_titulo"),UteisJSF.internacionalizar("per_Biometria_ajuda"), new String[]{"biometriaCons.xhtml","biometriaForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Layout Etiqueta
	*
	*/
	LAYOUT_ETIQUETA("LayoutEtiqueta", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LayoutEtiqueta_titulo"),UteisJSF.internacionalizar("per_LayoutEtiqueta_ajuda"), new String[]{"layoutEtiquetaCons.xhtml","layoutEtiquetaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Artefato Ajuda
	*
	*/
	ARTEFATO_AJUDA("ArtefatoAjuda", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ArtefatoAjuda_titulo"),UteisJSF.internacionalizar("per_ArtefatoAjuda_ajuda"), new String[]{"artefatoAjudaCons.xhtml","artefatoAjudaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Data Comemorativa
	*
	*/
	DATA_COMEMORATIVA("DataComemorativa", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_DataComemorativa_titulo"),UteisJSF.internacionalizar("per_DataComemorativa_ajuda"), new String[]{"dataComemorativaCons.xhtml","dataComemorativaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Controle Concorrencia Horario Turma
	*
	*/
	CONTROLE_CONCORRENCIA_HORARIO_TURMA("ControleConcorrenciaHorarioTurma", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ControleConcorrenciaHorarioTurma_titulo"),UteisJSF.internacionalizar("per_ControleConcorrenciaHorarioTurma_ajuda"), new String[]{"controleConcorrenciaHorarioTurmaCons.xhtml","controleConcorrenciaHorarioTurmaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Pais
	*
	*/
	PAIS("Pais", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Pais_titulo"),UteisJSF.internacionalizar("per_Pais_ajuda"), new String[]{"paizCons.xhtml","paizForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	/**
	* Catraca
	*
	*/
	CATRACA("Catraca", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Catraca_titulo"),UteisJSF.internacionalizar("per_Catraca_ajuda"), new String[]{"catracaCons.xhtml","catracaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	

	/**
	* Visao
	*
	*/
//	VISAO("Visao", new PermissaoVisao[] {
//			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Visao_titulo"),UteisJSF.internacionalizar("per_Visao_ajuda"), new String[]{"visaoCons.xhtml","visaoForm.xhtml"})
//			},
//			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
//			null, 
//			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONFIGURACOES),
	/**
	* Log
	*
	*/
	LOG("Log", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Log_titulo"),UteisJSF.internacionalizar("per_Log_ajuda"), new String[]{"logTrigger.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO),
	/**
	* Mapa Controle Atividades Usuarios
	*
	*/
	MAPA_CONTROLE_ATIVIDADES_USUARIOS("MapaControleAtividadesUsuarios", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MapaControleAtividadesUsuarios_titulo"),UteisJSF.internacionalizar("per_MapaControleAtividadesUsuarios_ajuda"), new String[]{"mapaControleAtividadesUsuariosCons.xhtml","mapaControleAtividadesUsuariosForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO),
	
	/**
	 * Relatorio SEI Decidir Administrativo
	 *
	 */
	RELATORIO_SEIDECIDIR_ADMINISTRATIVO("RelatorioSEIDecidirAdministrativo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirAdministrativo_titulo"),
					UteisJSF.internacionalizar("per_RelatorioSEIDecidirAdministrativo_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.RELATORIO, null, PerfilAcessoSubModuloEnum.ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO),
	PERMITIR_GERAR_RELATORIO_SEIDECIDIR_ADMINISTRATIVO_APENAS_DADOS("PermitirGerarRelatorioSeiDecidirAdministrativoApenasDados",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAdministrativoApenasDados_titulo"),
					UteisJSF.internacionalizar("per_PermitirGerarRelatorioSeiDecidirAdministrativoApenasDados_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_ADMINISTRATIVO,
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO),
	
	PERMITIR_VISUALIZAR_SCRIPT_SQL_RELATORIO_SEIDECIDIR_ADMINISTRATIVO("PermitirVisualizarScriptSqlRelatorioSeiDecidirAdministrativo",
			new PermissaoVisao[] { new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA,
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_titulo"),
					UteisJSF.internacionalizar("per_PermitirVisualizarScriptSqlRelatorioSeiDecidir_ajuda"),
					new String[] { "relatorioSEIDecidirRel.xhtml" }) },
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, RELATORIO_SEIDECIDIR_ADMINISTRATIVO,
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_RELATORIOS_ADMINISTRATIVO),
	/**
	* Perfil Acesso
	*
	*/
	PERFIL_ACESSO("PerfilAcesso", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PerfilAcesso_titulo"),UteisJSF.internacionalizar("per_PerfilAcesso_ajuda"), new String[]{"perfilAcessoCons.xhtml","perfilAcessoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO),
	/**
	* Usuario
	*
	*/
	USUARIO("Usuario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Usuario_titulo"),UteisJSF.internacionalizar("per_Usuario_ajuda"), new String[]{"usuarioCons.xhtml","usuarioForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO),
	UNIFICACAO_CADASTRO_USUARIO("UnificacaoCadastroUsuario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_UnificacaoCadastroUsuario_titulo"),UteisJSF.internacionalizar("per_UnificacaoCadastroUsuario_ajuda"), new String[]{"unificacaoCadastroUsuario.xhtml","unificacaoCadastroUsuario"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO),
	/**
	* Solicitar Alterar Senha
	*
	*/
	SOLICITAR_ALTERAR_SENHA("SolicitarAlterarSenha", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_SolicitarAlterarSenha_titulo"),UteisJSF.internacionalizar("per_SolicitarAlterarSenha_ajuda"), new String[]{"solicitarAlterarSenhaCons.xhtml","solicitarAlterarSenhaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO),
	MAPA_EMAIL("MapaEmail", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MapaEmail_titulo"),UteisJSF.internacionalizar("per_MapaEmail_ajuda"), new String[]{"mapaEmailForm.xhtml","mapaEmailForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),			
	/**
	* Unidade Ensino
	*
	*/
	UNIDADE_ENSINO("UnidadeEnsino", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_UnidadeEnsino_titulo"),UteisJSF.internacionalizar("per_UnidadeEnsino_ajuda"), new String[]{"unidadeEnsinoCons.xhtml","unidadeEnsinoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	AGRUPAMENTO_UNIDADE_ENSINO("AgrupamentoUnidadeEnsino", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AgrupamentoUnidadeEnsino_titulo"),UteisJSF.internacionalizar("per_AgrupamentoUnidadeEnsino_ajuda"), new String[]{"agrupamentoUnidadeEnsinoCons.xhtml","agrupamentoUnidadeEnsinoForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	/**
	* Follow Me
	*
	*/
	FOLLOW_ME("FollowMe", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FollowMe_titulo"),UteisJSF.internacionalizar("per_FollowMe_ajuda"), new String[]{"followMeCons.xhtml","followMeForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	/**
	* Configuracao Atendimento
	*
	*/
	CONFIGURACAO_ATENDIMENTO("ConfiguracaoAtendimento", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoAtendimento_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoAtendimento_ajuda"), new String[]{"configuracaoAtendimentoCons.xhtml","configuracaoAtendimentoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ATENDIMENTO_ALUNO),
	/**
	* Personalizacao Mensagem Automatica
	*
	*/
	PERSONALIZACAO_MENSAGEM_AUTOMATICA("PersonalizacaoMensagemAutomatica", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PersonalizacaoMensagemAutomatica_titulo"),UteisJSF.internacionalizar("per_PersonalizacaoMensagemAutomatica_ajuda"), new String[]{"personalizacaoMensagemAutomaticaCons.xhtml","personalizacaoMensagemAutomaticaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	/**
	* Layout Relatorio SEI Decidir
	*
	*/
	LAYOUT_RELATORIO_SEIDECIDIR("LayoutRelatorioSEIDecidir", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LayoutRelatorioSEIDecidir_titulo"),UteisJSF.internacionalizar("per_LayoutRelatorioSEIDecidir_ajuda"), new String[]{"layoutRelatorioSEIDecidirCons.xhtml","layoutRelatorioSEIDecidirForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	/**
	* Tipagem Ouvidoria
	*
	*/
	TIPAGEM_OUVIDORIA("TipagemOuvidoria", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_TipagemOuvidoria_titulo"),UteisJSF.internacionalizar("per_TipagemOuvidoria_ajuda"), new String[]{"tipagemOuvidoriaCons.xhtml","tipagemOuvidoriaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ATENDIMENTO_ALUNO),
	/**
	* Grupo Destinatarios
	*
	*/
	GRUPO_DESTINATARIOS("GrupoDestinatarios", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_GrupoDestinatarios_titulo"),UteisJSF.internacionalizar("per_GrupoDestinatarios_ajuda"), new String[]{"grupoDestinatariosCons.xhtml","grupoDestinatariosForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	/**
	* Funcionario
	*
	*/
	FUNCIONARIO("Funcionario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_titulo"),UteisJSF.internacionalizar("per_Funcionario_ajuda"), new String[]{"funcionarioCons.xhtml","funcionarioForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	FUNCIONARIO_UNIFICAR_FUNCIONARIOS_DUPLICADOS("Funcionario_unificarFuncionariosDuplicados", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionariounificarFuncionariosDuplicados_titulo"),UteisJSF.internacionalizar("per_FuncionariounificarFuncionariosDuplicados_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	FUNCIONARIO_PERMITIR_CRIAR_USUARIO("Funcionario_permitirCriarUsuario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionariopermitirCriarUsuario_titulo"),UteisJSF.internacionalizar("per_FuncionariopermitirCriarUsuario_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	FUNCIONARIO_ADICIONAR_CARGO("Funcionario_adicionarCargo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_adicionarCargo_titulo"),UteisJSF.internacionalizar("per_Funcionario_adicionarCargo_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	FUNCIONARIO_ALTERAR_SITUACAO_CARGO("Funcionario_alterarSituacaoCargo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_alterarSituacaoCargo_titulo"),UteisJSF.internacionalizar("per_Funcionario_alterarSituacaoCargo_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	FUNCIONARIO_CADASTRAR_EVENTOS("Funcionario_cadastrarEventos", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_cadastrarEventos_titulo"),UteisJSF.internacionalizar("per_Funcionario_cadastrarEventos_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),

	FUNCIONARIO_EVENTO_VALE_TRANSPORTE("Funcionario_acessarValeTransporte", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_acessarValeTransporte_titulo"),UteisJSF.internacionalizar("per_Funcionario_acessarValeTransporte_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),

	FUNCIONARIO_EVENTO_SALARIO_COMPOSTO("Funcionario_acessarSalarioComposto", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_acessarSalarioComposto_titulo"),UteisJSF.internacionalizar("per_Funcionario_acessarSalarioComposto_ajuda"))
			},	
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),

	FUNCIONARIO_MARCACAO_FERIAS("Funcionario_acessarMarcacaoFerias", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_acessarMarcacaoFerias_titulo"),UteisJSF.internacionalizar("per_Funcionario_acessarMarcacaoFerias_ajuda"))
			},	
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),

	FUNCIONARIO_PERIODO_AQUISITIVO("Funcionario_acessarPeriodoAquisitivo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_acessarPeriodoAquisitivo_titulo"),UteisJSF.internacionalizar("per_Funcionario_acessarPeriodoAquisitivo_ajuda"))
			},	
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	FUNCIONARIO_EVENTO_EMPRESTIMO("Funcionario_acessarEventoEmprestimo", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Funcionario_acessarEventoEmprestimo_titulo"),UteisJSF.internacionalizar("per_Funcionario_acessarEventoEmprestimo_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FUNCIONARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	/**
	* Departamento
	*
	*/
	DEPARTAMENTO("Departamento", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Departamento_titulo"),UteisJSF.internacionalizar("per_Departamento_ajuda"), new String[]{"departamentoCons.xhtml","departamentoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	/**
	* Cargo
	*
	*/
	CARGO("Cargo", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Cargo_titulo"),UteisJSF.internacionalizar("per_Cargo_ajuda"), new String[]{"cargoCons.xhtml","cargoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	/**
	* Configuracao Mobile
	*
	*/
	CONFIGURACAO_MOBILE("ConfiguracaoMobile", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoMobile_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoMobile_ajuda"), new String[]{"configuracaoMobileCons.xhtml","configuracaoMobileForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	
	CONFIGURACAO_SEI_GSUITE("ConfiguracaoSeiGsuite", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoSeiGsuite_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoSeiGsuite_ajuda"), new String[]{"configuracaoSeiGsuiteCons.xhtml","ConfiguracaoSeiGsuiteForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	
	CONFIGURACAO_SEI_BLACKBOARD("ConfiguracaoSeiBlackboard", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoSeiBlackboard_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoSeiBlackboard_ajuda"), new String[]{"configuracaoSeiBlackboardCons.xhtml","ConfiguracaoSeiBlackboardForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),

	/**
	 * Ouvidoria
	 *
	 */
	OUVIDORIA("Ouvidoria", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_Ouvidoria_titulo"),UteisJSF.internacionalizar("per_Ouvidoria_ajuda"), new String[]{"ouvidoriaProfessorForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_Ouvidoria_titulo"),UteisJSF.internacionalizar("per_Ouvidoria_ajuda"), new String[]{"ouvidoriaCoordenadorForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Ouvidoria_titulo"),UteisJSF.internacionalizar("per_Ouvidoria_ajuda"), new String[]{"ouvidoriaCons.xhtml","ouvidoriaForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_Ouvidoria_titulo"),UteisJSF.internacionalizar("per_Ouvidoria_ajuda"), new String[]{"ouvidoriaAlunoForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.PAIS, UteisJSF.internacionalizar("per_Ouvidoria_titulo"),UteisJSF.internacionalizar("per_Ouvidoria_ajuda"), new String[]{"ouvidoriaAlunoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	
	/**
	 * Simular Acesso Usuario
	 *
	 */
	SIMULAR_ACESSO_USUARIO("SimularAcessoUsuario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_SimulacaoAcesso_usuario"),UteisJSF.internacionalizar("per_ComunicacaoInterna_ajuda"), new String[]{"administrativo.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	
	/**
	 * Biblioteca
	 *
	 */
	IMPRESSORA("Impressora", new PermissaoVisao[] {			 
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_Impressora_titulo"),UteisJSF.internacionalizar("per_Impressora_ajuda"), new String[]{"impressoraCons.xhtml","impressoraForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	
	POOL_IMPRESSAO("PoolImpressao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PoolImpressao_titulo"),UteisJSF.internacionalizar("per_PoolImpressao_ajuda"), new String[]{"impressoraCons.xhtml","impressoraForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.IMPRESSORA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	
	PLANO_CONTA("PlanoConta", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PlanoConta_titulo"),UteisJSF.internacionalizar("per_PlanoConta_ajuda"), new String[]{"planoContaCons.xhtml","planoContaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),
	
	LAYOUT_INTEGRACAO("LayoutIntegracao", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_LayoutIntegracaoXml_titulo"),UteisJSF.internacionalizar("per_LayoutIntegracaoXml_ajuda"), new String[]{"layoutIntegracaoCons.xhtml","layoutIntegracaoForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),
	
	INTEGRACAO_CONTABIL("IntegracaoContabil", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_IntegracaoContabil_titulo"),UteisJSF.internacionalizar("per_IntegracaoContabil_ajuda"), new String[]{"integracaoContabilCons.xhtml","integracaoContabilForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),
	//
	INTEGRACAO_CONTABIL_PERMITIR_APENAS_COM_PERIODO_FECHAMENTO_MES("IntegracaoContabilPermitirApenasComPeriodoFechamentoMes", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_IntegracaoContabilPermitirApenasComPeriodoFechamentoMes_titulo"),UteisJSF.internacionalizar("per_IntegracaoContabilPermitirApenasComPeriodoFechamentoMes_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.INTEGRACAO_CONTABIL, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),
	
	FECHAMENTO_MES("FechamentoMes", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FechamentoMes_titulo"),UteisJSF.internacionalizar("per_FechamentoMes_ajuda"), new String[]{"fechamentoMesCons.xhtml","fechamentoMesForm.xhtml"})
	        },
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),	

	FUNCIONARIO_MES_LIBERAR_BLOQUEIO_INCLUIR_ALTERAR_CONTARECEBER("FuncionarioMes_liberarBloqueioIncluirAlterarContaReceber", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarContaReceber_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarContaReceber_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),	
	
	FUNCIONARIO_MES_LIBERAR_BLOQUEIO_RECEBIMENTO_CONTARRECEBER("FuncionarioMes_liberarBloqueioRecebimentoContaReceber", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioRecebimentoContaReceber_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioRecebimentoContaReceber_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),		
	
	FUNCIONARIO_MES_LIBERAR_BLOQUEIO_INCLUIR_ALTERAR_CONTARPAGAR("FuncionarioMes_liberarBloqueioIncluirAlterarContaPagar", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarContaPagar_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarContaPagar_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),		
	
	FUNCIONARIO_MES_LIBERAR_BLOQUEIO_PAGAMENTO_CONTARPAGAR("FuncionarioMes_liberarBloqueioPagamentoContaPagar", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioPagamentoContaPagar_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioPagamentoContaPagar_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),	
	
	FUNCIONARIO_MES_LIBERAR_INCLUIR_ALTERAR_NFENTRADA("FuncionarioMes_liberarBloqueioIncluirAlterarNFEntrada", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarNFEntrada_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarNFEntrada_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),		
	
	FUNCIONARIO_MES_LIBERAR_INCLUIR_ALTERAR_NFSAIDA("FuncionarioMes_liberarBloqueioIncluirAlterarNFSaida", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarNFSaida_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarNFSaida_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),		
	
	FUNCIONARIO_MES_LIBERAR_INCLUIR_ALTERAR_MOVIMENTACAO_FINANCEIRA("FuncionarioMes_liberarBloqueioIncluirAlterarMovimentacaoFinanceira", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarMovimentacaoFinanceira_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioIncluirAlterarMovimentacaoFinanceira_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),		

	FUNCIONARIO_MES_LIBERAR_ABERTURA_CAIXA("FuncionarioMes_liberarBloqueioAberturaCaixa", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioAberturaCaixa_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesLiberarBloqueioAberturaCaixa_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),		

	FUNCIONARIO_MES_LIBERAR_CANCELAR_FECHAMENTO_MES_REALIZADO("FuncionarioMes_cancelarFechamentoMesRealizado", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_FuncionarioMesCancelarFechamentoMesRealizado_titulo"),UteisJSF.internacionalizar("per_FuncionarioMesCancelarFechamentoMesRealizado_titulo"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.FECHAMENTO_MES, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL),		
	
	CONFIGURACAO_CONTABIL("ConfiguracaoContabil", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoContabil_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoContabil_ajuda"), new String[]{"configuracaoContabilCons.xhtml","configuracaoContabilForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTABIL), 
	
	MAPA_DOCUMENTO_ASSINADO_PESSOA("MapaDocumentoAssinadoPessoa", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_MapaDocumentoAssinadoPessoa_titulo"),UteisJSF.internacionalizar("per_MapaDocumentoAssinadoPessoa_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_MapaDocumentoAssinadoPessoa_titulo"),UteisJSF.internacionalizar("per_MapaDocumentoAssinadoPessoa_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaProfessorForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_MapaDocumentoAssinadoPessoa_titulo"),UteisJSF.internacionalizar("per_MapaDocumentoAssinadoPessoa_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaCoordenadorForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO), 
	
	PERMITIR_VISUALIZAR_DOCUMENTO_ASSINADO_OUTRAS_PESSOAS("PemitirVisualizarDocumentoAssinadoOutrasPessoas", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PemitirVisualizarDocumentoAssinadoOutrasPessoas_titulo"),UteisJSF.internacionalizar("per_PemitirVisualizarDocumentoAssinadoOutrasPessoas_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PemitirVisualizarDocumentoAssinadoOutrasPessoas_titulo"),UteisJSF.internacionalizar("per_PemitirVisualizarDocumentoAssinadoOutrasPessoas_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaProfessorForm.xhtml"}),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PemitirVisualizarDocumentoAssinadoOutrasPessoas_titulo"),UteisJSF.internacionalizar("per_PemitirVisualizarDocumentoAssinadoOutrasPessoas_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaCoordenadorForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			MAPA_DOCUMENTO_ASSINADO_PESSOA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),

	PERMITIR_ALTERAR_ASSINANTE("PermitirAlterarAssinante", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_PemitirAlterarAssinante_titulo"),UteisJSF.internacionalizar("per_PemitirAlterarAssinante_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaForm.xhtml"}),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PemitirAlterarAssinante_titulo"),UteisJSF.internacionalizar("per_PemitirAlterarAssinante_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaProfessorForm.xhtml"}),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_PemitirAlterarAssinante_titulo"),UteisJSF.internacionalizar("per_PemitirAlterarAssinante_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaCoordenadorForm.xhtml"})
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE,
			MAPA_DOCUMENTO_ASSINADO_PESSOA,
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	
	PERMITIR_ASSINATURA_DIARIO_PROFESSOR_APOS_ASSINATURA_COORDENADOR("PermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso", new PermissaoVisao[] {			 
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_PermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso_titulo"),UteisJSF.internacionalizar("per_PermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso_ajuda"), new String[]{"mapaDocumentoAssinadoPessoaProfessorForm.xhtml"})			 
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			MAPA_DOCUMENTO_ASSINADO_PESSOA, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO),
	USUARIO_PERMITIR_ALTERAR_PESSOA_USUARIO("Usuario_permitirAlterarPessoaUsuario", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_UsuarioPermitirAlterarPessoaUsuario_titulo"),UteisJSF.internacionalizar("per_UsuarioPermitirAlterarPessoaUsuario_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.USUARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO), 
	CONFIGURACAO_APARENCIA_SISTEMA("ConfiguracaoAparenciaSistema", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_ConfiguracaoAparenciaSistema_titulo"),UteisJSF.internacionalizar("per_ConfiguracaoAparenciaSistema_ajuda"), new String[] {"configuracaoAparenciaSistemaForm.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, 
			null, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	
	VISAO_APRESENTAR_LINKSUTEIS("Visao_apresentar_linksUteis", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_titulo"),UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_titulo"),UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_titulo"),UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_ajuda")),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_titulo"),UteisJSF.internacionalizar("per_VisaoApresentarLinksUteisADM_ajuda"))
	},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			PerfilAcessoPermissaoAdministrativoEnum.USUARIO, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO), 
	
	REGISTRO_LDAP("RegistroLdap", new PermissaoVisao[]{
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_RegistroLdap_titulo"), UteisJSF.internacionalizar("per_RegistroLdap_ajuda"), new String[]{"registroLdap.xhtml"})
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE,
			null,
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_CONTROLE_ACESSO),
	
	ACESSO_DADOS("AcessoDados", new PermissaoVisao[] {
			new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_AcessoDados_titulo"),
					UteisJSF.internacionalizar("per_AcessoDados_ajuda"), new String[] { "" }),
			new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AcessoDados_titulo"),
					UteisJSF.internacionalizar("per_AcessoDados_ajuda"), new String[] { "" }), 
			new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_AcessoDados_titulo"),
					UteisJSF.internacionalizar("per_AcessoDados_ajuda"), new String[] { "" }),
			new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AcessoDados_titulo"),
					UteisJSF.internacionalizar("per_AcessoDados_ajuda"), new String[] { "" })
	
			},
			TipoPerfilAcessoPermissaoEnum.ENTIDADE, null, PerfilAcessoSubModuloEnum.ADMINISTRATIVO_BASICO),
	
	PERMITIR_ENVIAR_EMAIL_DADOS_LGPD("AcessoDados_permitirEnviarEmailDadosLGPD", new PermissaoVisao[] {
			 new PermissaoVisao(TipoVisaoEnum.ADMINISTRATIVA, UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_titulo"), UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.ALUNO, UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_titulo"), UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.COORDENADOR, UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_titulo"), UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_ajuda")),
			 new PermissaoVisao(TipoVisaoEnum.PROFESSOR, UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_titulo"), UteisJSF.internacionalizar("per_AcessoDadosPermitirEnviarEmailDadosLGPD_ajuda"))
			},
			TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, 
			ACESSO_DADOS, 
			PerfilAcessoSubModuloEnum.ADMINISTRATIVO_ADMINISTRATIVO);
	

	/**
	 * @param descricao
	 * @param ajuda
	 * @param paginaAcesso
	 * @param tipoPerfilAcesso
	 * @paran permissaoSuperiorEnum - popular quando no tipoPerfilAcesso for funcionalidade 
	 */
	private PerfilAcessoPermissaoAdministrativoEnum(String valor, PermissaoVisao[] permissaoVisao, 
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
