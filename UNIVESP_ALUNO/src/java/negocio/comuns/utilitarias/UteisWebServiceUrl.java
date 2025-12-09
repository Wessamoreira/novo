package negocio.comuns.utilitarias;

public class UteisWebServiceUrl {
	public static final String Authorization = "Authorization";
	public static final String Content_Type ="Content-Type"; 
	public static final String Application_X_WWW_FORM ="application/x-www-form-urlencoded";
	public static final String Application_JSON = "application/json";
	public static final String token = "token";
	public static final String ACS_Authorization_Token = "ACS-Authorization-Token";
	
	public static final String code ="code";
	public static final String id ="id";
	public static final String codigo ="codigo";
	public static final String ul ="ul";
	public static final String descricao ="descricao";
	public static final String pessoagsuite ="pessoagsuite";
	public static final String statuspessoagsuite ="statuspessoagsuite";
	public static final String emailpessoagsuite ="emailpessoagsuite";
	public static final String senhapessoagsuite ="senhapessoagsuite";
	
	public static final String emailpessoainstitucional ="emailpessoainstitucional";
	public static final String tipoSalaAulaBlackboardPessoaEnum ="tipoSalaAulaBlackboardPessoaEnum";
	
	public static final String pessoa ="pessoa";
	public static final String matricula ="matricula";
	public static final String matriculaperiodoturmadisciplina ="matriculaperiodoturmadisciplina";
	public static final String unidadeEnsino ="unidadeEnsino";
	public static final String criarContaFuncionario ="criarContaFuncionario";
	public static final String criarContaAluno ="criarContaAluno";
	public static final String horarioTurma ="horarioTurma";
	public static final String data ="data";
	public static final String operacao ="operacao";
	
	public static final String URL_SEI_WEB_SERVICE = "/webservice";
	public static final String URL_SEI_SERVICO_GESTAO_ENVIO_MENSAGEM = "/gestaoEnvioMsgWS";
	public static final String URL_SEI_SERVICO_MENSAGEM_GSUITE_CONTA = "/mgsContaGsuite";
	public static final String URL_SEI_SERVICO_GOOGLE_MEET_CONVIDADO = "/googleMeetConvidadoWS";
	public static final String URL_SEI_SERVICO_GOOGLE_MEET_CONVIDADO_CONSULTAR = "/consultaGoogleMeetConvidado";
	
	public static final String URL_SEI_SERVICO_PESSOA_GSUITE = "/pessoaGuiteWS";
	public static final String URL_SEI_SERVICO_PESSOA_GSUITE_HISTORICO = "/consultaPessoaGsuiteHistorico";
	public static final String URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_NOTAS = "/salaAulaBlackboardPessoaNotaRS";
	public static final String URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_APURAR = "/salaAulaBlackboardPessoaNotaApurar";
	public static final String URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_OPERACAO = "/salaAulaBlackboardOperacaoRS";
	public static final String URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_OPERACAO_GESTAO_MENSAGEM = "/salaAulaBlackboardGestaoMensagem";
	
	public static final String URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_PESSOA = "/salaAulaBlackboardPessoaRS";
	public static final String URL_SEI_SERVICO_SALA_AULA_BLACKBOARD_PESSOA_HISTORICO = "/consultaSalaAulaBlackboardPessoaRSHistorico";
	
	public static final String URL_SEI_SERVICO_PESSOA_EMAIL_INSTITUCIONAL = "/pessoaEmailInstitucionalRS";
	public static final String URL_SEI_SERVICO_PESSOA_EMAIL_INSTITUCIONAL_HISTORICO = "/consultaPessoaEmailInstitucionalHistorico";
	
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN = "/certiSign";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_CALLBACK_DOCUMENTO = "/callBackDocumento";	
	//public static final String URL_SEI_WEB_SERVICE_CERTISIGN_URL = "https://api-sbx.portaldeassinaturas.com.br";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_UPLOAD = "/api/v2/document/upload";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_BLOQUEIO = "/api/v2/document/batchBlock";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_CREATE_DOCUMENT = "/api/v2/document/create";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_PACKAGE = "/api/v2/document/package";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_DELETE = "/api/v2/document/delete";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_SEND_REMINDER = "/api/v2/document/SendReminder";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_PARTICIPANT_DISCARD = "/api/v2/document/participantDiscard";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_PARTICIPANT_ADD = "/api/v2/document/participantAdd";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_VALIDATE_SIGNATURES  = "/api/v2/document/ValidateSignatures";
	public static final String URL_SEI_WEB_SERVICE_CERTISIGN_FLOW_ACTIONS = "/api/v2/document/flowActions";
	
	
	public static final String URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_CONSULTAR = "/consultaMapaDocumentoPendente";
	public static final String URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_CONSULTAR_ASSINADO = "/consultaMapaDocumentoAssinado";
	public static final String URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_REJEITAR = "/rejeitarMapaDocumentoAssinado";	
	public static final String URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILE = "/fileMapaDocumentoAssinado";
	public static final String URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILE_EXISTE = "/fileExisteMapaDocumentoAssinado";
	public static final String URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILE_UPLOAD = "/fileUploadMapaDocumentoAssinado";
	public static final String URL_SEI_SERVICO_MAPA_DOCUMENTO_ASSINADO_FILTRO_TIPO_ORIGEM_DOCUMENTO = "/fitroTipoOrigemDocumentoAssinado";
	


	public static final String URL_GSUITE_TOKEN = "/oauth/token";
	public static final String URL_GSUITE_SERVICO_STATUS = "/status";
	public static final String URL_GSUITE_CREDENCIAL = "/credencialGoogler";
	public static final String URL_GSUITE_ADMIN_SDK = "/adminSDK";
	public static final String URL_GSUITE_ADMIN_SDK_CONTA_INDIVIDUAL = "/contaIndividualPessoa";
	public static final String URL_GSUITE_ADMIN_SDK_CONTA_MATRICULA = "/contaIndividualMatricula";
	public static final String URL_GSUITE_ADMIN_SDK_CONTA_LOTE = "/contaLotePessoa";
	public static final String URL_GSUITE_ADMIN_SDK_CONTA_VALIDA = "/contaValida";
	public static final String URL_GSUITE_ADMIN_SDK_IMPORTA_LOTE = "/importaLotePessoa";
	public static final String URL_GSUITE_ADMIN_SDK_CONTA_SENHA = "/atualizarSenhaContaIndividual";
	
	public static final String URL_GOOGLE_MEET = "/googleMeet";
	public static final String URL_GOOGLE_MEET_PERSISTIR = "/persistir";
	public static final String URL_GOOGLE_MEET_AVULSO = "/persistirAvulso";
	public static final String URL_GOOGLE_MEET_ATUALIZACAO = "/atualizacao";
	public static final String URL_GOOGLE_MEET_INCLUIR = "/incluir";
	public static final String URL_GOOGLE_MEET_EXCLUIR = "/excluir";
	public static final String URL_GOOGLE_MEET_CONSULTAR = "/consultarGoogleMeet";
	public static final String URL_GOOGLE_MEET_LOTE_NAO_PROCESSADO = "/loteNaoProcessado";
	public static final String URL_CONSULTAR_GOOGLE_MEET_PARA_PROCESSAMENTO = "/consultarGoogleMeetParaProcessamento";
	public static final String URL_GOOGLE_MEET_GERAR_EVENTOS_CALENDAR = "/gerarEventosCalendar";	
	public static final String URL_GOOGLE_MEET_CORRECAO_DONO = "/googleMeetCorrecaoDono";
	
	public static final String URL_CLASSROOM_GOOGLE = "/classroomGoogle";
	public static final String URL_CLASSROOM_GOOGLE_PERSISTIR = "/classroomGooglePersistir";
	public static final String URL_CLASSROOM_GOOGLE_EXCLUIR = "/classroomGoogleExcluir";
	public static final String URL_CLASSROOM_GOOGLE_EAD_EXCLUIR = "/classroomGoogleEadExcluir";
	public static final String URL_CLASSROOM_GOOGLE_REALIZAR_REVISAO= "/adicionarRealizarRevisao";
	public static final String URL_CLASSROOM_GOOGLE_ATUALIZAR_PROFESSOR_AUXILIAR= "/atualizarProfessorAuxiliar";
	public static final String URL_CLASSROOM_GOOGLE_REALIZAR_REVISAO_TURMA= "/adicionarRealizarRevisaoTurma";
	public static final String URL_CLASSROOM_GOOGLE_CONSULTAR_STUDENTS = "/consultarStudents";
	public static final String URL_CLASSROOM_GOOGLE_CONSULTAR_STUDENT = "/consultarStudent";
	public static final String URL_CLASSROOM_GOOGLE_ADICIONAR_STUDENTS = "/adicionarStudents";
	public static final String URL_CLASSROOM_GOOGLE_ATUALIZACAO_STUDENTS = "/atualizacaoStudents";
	public static final String URL_CLASSROOM_GOOGLE_CONTA_LOTE = "/classroomContaLote";
	public static final String URL_CLASSROOM_GOOGLE_CONTA_LOTE_HORARIO_TURMA = "/classroomContaLoteHorarioTurma";
	public static final String URL_CLASSROOM_GOOGLE_ALTERACAO_DONO_DRIVE = "/classroomAlteracaoDonoDrive";
	public static final String URL_CLASSROOM_OPERACAO_STUDENTS = "/classroomOperacaoLoteStudent";
	public static final String URL_CLASSROOM_GOOGLE_CONTA_LOTE_CORRECAO_NOME = "/classroomContaLoteCorrecaoNome";
	
	
	
	public static final String URL_PIX_TOKEN = "/oauth/token";
	public static final String URL_PIX_WEBHOOK = "/pixWebhook";
	public static final String URL_PIX_COBV = "/pix/v1/cob/";
	public static final String URL_PIX_COBVQRC = "/pix/v1/cobqrcode/";
	
	public static final String URL_PIX_RECEBIMENTOS_COB = "/pix_recebimentos/v2/cob/";
	public static final String URL_PIX_RECEBIMENTOS_WEBHOOK = "/pix_recebimentos/v2/webhook/";

	
	
	
	public static final String URL_BLACKBOARD_TOKEN_SEI = "/oauth/token";
	public static final String URL_BLACKBOARD_SERVICO_STATUS_SEI = "/status";
	public static final String URL_BLACKBOARD_SERVICO_STATUS_BLACKBOARD = "/statusBlackboard";
	
	public static final String URL_BLACKBOARD_ADMIN_SDK = "/adminSDK";
	public static final String URL_BLACKBOARD_ADMIN_SDK_CONTA_INDIVIDUAL = "/contaIndividual";
	public static final String URL_BLACKBOARD_ADMIN_SDK_PERMISAO = "/permissao";
	public static final String URL_BLACKBOARD_ADMIN_SDK_DATA_SOURCE = "/dataSource";
	
	
	public static final String URL_BLACKBOARD_SALAAULA = "/salaAula";
	public static final String URL_BLACKBOARD_SALAAULA_PERSISTIR = "/salaAulaPersistir";
	public static final String URL_BLACKBOARD_SALAAULA_CONTA_LOTE = "/salaAulaContaLote";
	public static final String URL_BLACKBOARD_SALAAULA_PERSISTIR_GRUPO = "/salaAulaPersistirGrupo";
	public static final String URL_BLACKBOARD_SALAAULA_CONSULTAR = "/salaAulaConsultar";
	public static final String URL_BLACKBOARD_SALAAULA_COPIA_CONTEUDO = "/salaAulaConsultarCopia";
	public static final String URL_BLACKBOARD_SALAAULA_CONSULTAR_CONTEUDO = "/salaAulaConsultarConteudo";
	public static final String URL_BLACKBOARD_SALAAULA_CONTA_LOTE_HORARIO_TURMA = "/salaAulaContaLoteHorarioTurma";
	public static final String URL_BLACKBOARD_SALAAULA_EXCLUIR = "/salaAulaExcluir";
	public static final String URL_BLACKBOARD_SALAAULA_EAD_EXCLUIR = "/salaAulaEadExcluir";
	public static final String URL_BLACKBOARD_SALAAULA_GRUPO_OPERACAO_ALUNOS = "/salaAulaGrupoOperacaoAlunos";
	public static final String URL_BLACKBOARD_SALAAULA_ADICIONAR_ALUNOS = "/salaAulaAdicionarAlunos";
	public static final String URL_BLACKBOARD_SALAAULA_ATUALIZAR_ALUNOS = "/salaAulaAtualizarAlunos";
	public static final String URL_BLACKBOARD_SALAAULA_CONSULTAR_ALUNOS = "/salaAulaConsultarAlunos";
	public static final String URL_BLACKBOARD_SALAAULA_CONSULTAR_ALUNO = "/salaAulaConsultarAluno";
	public static final String URL_BLACKBOARD_SALAAULA_OPERACAO_ALUNO = "/salaAulaOperacaoAluno";
	public static final String URL_BLACKBOARD_SALAAULA_OPERACAO_CODIGO = "/salaAulaOperacaoCodigo";
	public static final String URL_BLACKBOARD_SALAAULA_REALIZAR_REVISAO = "/salaAulaRealizarRevisao";
	public static final String URL_BLACKBOARD_SALAAULA_REALIZAR_REVISAO_TURMA = "/salaAulaRealizarRevisaoTurma";
	public static final String URL_BLACKBOARD_SALAAULA_REVISAO_SEI = "/salaAulaRevisaoSei";
	public static final String URL_BLACKBOARD_IMPORTAR_SALAAULA = "/importarSalaAula";
	public static final String URL_BLACKBOARD_GERAR_SALAS = "/gerarSalas";
	public static final String URL_BLACKBOARD_SALAAULA_ADICIONAR_PESSOAS = "/salaAulaAdicionarPessoas";
	public static final String URL_BLACKBOARD_SALAAULA_REMOVER_PESSOAS = "/salaAulaRemoverPessoas";
	public static final String URL_BLACKBOARD_SALAAULA_CONSULTAR_NOTAS_ALUNOS = "/salaAulaConsultarNotasAlunos";
	public static final String REALIZAR_ESCRITA_DEBUG = "/realizarEscritaDebug";

	public static final String URL_SEI_WEB_SERVICE_TECHCERT = "/techcert";
	public static final String URL_SEI_WEB_SERVICE_TECHCERT_DOCUMENTO = "/documento";
	public static final String URL_TECHCERT_FOLDERS = "/api/folders";
	public static final String URL_TECHCERT_UPLOADS = "/api/uploads";
	public static final String URL_TECHCERT_UPLOADS_BYTES = "/api/uploads/bytes";
	public static final String URL_TECHCERT_DOCUMENTS_FLOWS = "/api/document-flows";
	public static final String URL_TECHCERT_DOCUMENTS = "/api/documents";
	public static final String URL_TECHCERT_BATCH_FOLDER = "/batch/folder";
	public static final String URL_TECHCERT_ACTION_URL = "/action-url";
	public static final String URL_TECHCERT_KEYS = "/api/documents/keys";
	public static final String URL_TECHCERT_USERS = "/api/users";
	public static final String URL_TECHCERT_NOTIFY_PENDING = "/notify-pending";
	public static final String URL_TECHCERT_NOTIFICATIONS_FLOW_ACTION_REMINDER = "/flow-action-reminder";
	public static final String URL_TECHCERT_FLOW = "/flow";
	public static final String URL_TECHCERT_CONTENT_64 = "/content-b64";
	
	public static final String URL_AUREA_KNOWLEDGE = Constantes.BARRA + "constants" + Constantes.BARRA + "knowledge";
	public static final String URL_AUREA_SELECT_ITEMS_SUBJECTS = Constantes.BARRA + "select-items" + Constantes.BARRA + "subjects";
	public static final String URL_AUREA_SELECT_ITEMS_CATEGORIES = Constantes.BARRA + "select-items" + Constantes.BARRA + "categories";
	
}
