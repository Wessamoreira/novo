package negocio.interfaces.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.TextoPadraoBancoCurriculumVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ComunicacaoInternaInterfaceFacade {

    public ComunicacaoInternaVO novo() throws Exception;

    public void incluir(ComunicacaoInternaVO obj, boolean controlarAcesso,UsuarioVO usuario,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO) throws Exception;

    public void responder(final ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void alterar(final ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void excluir(ComunicacaoInternaVO obj, UsuarioVO usuarioVO) throws Exception;

    public void registrarLeituraComunicadoInterno(ComunicacaoInternaVO obj, PessoaVO pessoa, UsuarioVO usuarioLogado) throws Exception;

    public void registrarRespostaComunicadoInterno(ComunicacaoInternaVO obj, PessoaVO pessoa) throws Exception;

    public ComunicacaoInternaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarCodigoResponsavel(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    
    public List consultarPorSituacaoEntrada(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorTipoComunicadoInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorTipoDestinatario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List consultarPorEntradaLimite(Integer codigo, String tipoDestinatario, Integer limite, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public void consultaRapidaPorEntradaLimiteVisaoCoordenador(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception;

    public void consultaRapidaPorEntradaLimiteVisaoProfessor(DataModelo controleConsulta,  Integer codigo, String tipoDestinatario, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception ;

    public void consultaRapidaPorEntradaLimiteVisaoAluno(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData, boolean somenteNaoLidas) throws Exception;

    public List consultarPorTipoComunicadoInternoMuralDestinatario(String valorConsulta, Integer destinatario, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarPorTipoComunicadoInternoNaoLidaRespondidaDestinatario(String valorConsulta, Integer destinatario, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultarParaMuralComunicacaoInterna(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public List consultaComunicadoInternoDestinatario(ComunicacaoInternaVO obj, boolean controlarAcesso,UsuarioVO usuario) throws Exception;

    public List<ComunicadoInternoDestinatarioVO> consultarListaDeFuncionariosDestinatarios(List<FuncionarioVO> lista, Integer unidadeEnsino,UsuarioVO usuario) throws Exception;

    public void validarUsuarioConsultarMeusProfessores(UsuarioVO usuario) throws Exception;

    public void validarUsuarioConsultarMeusAmigos(UsuarioVO usuario) throws Exception;

    public void consultaRapidaPorCodigoResponsavel(DataModelo controleConsulta, Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception;

    public void carregarDados(ComunicacaoInternaVO obj,UsuarioVO usuario) throws Exception;

    public ComunicacaoInternaVO inicializarDadosRespotaComunicado(ComunicacaoInternaVO obj, UsuarioVO usuario) throws Exception;

    public List<ComunicacaoInternaVO> consultaRapidaPorEntradaLimiteMarketingLeituraObrigatoria(Integer codigo, Integer limite, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public void validarTipoMarketing(ComunicacaoInternaVO obj);

    public void validarTipoLeituraObrigatoria(ComunicacaoInternaVO obj);

    public void alterarTipoMarketingAposVisualizacao(ComunicacaoInternaVO comunicacaoInternaVO) throws Exception;

    public void alterarTipoLeituraObrigtoriaAPosLeitura(ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuarioLogado) throws Exception;

    public List<ComunicacaoInternaVO> consultaRapidaComunicacaoInternaNaoLidas(Integer codUsuarioLogado, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

    public void alterarRemoverCaixaSaida(final Integer comunicado, final Boolean remover, UsuarioVO usuarioVO) throws Exception;

    /** Este método tem a função de criar um comunicado interno onde no corpo da mensagem será utilizado o atrinbuto frase,
     *  os destinários serão montados conforme os outros parâmentros onde se passar como true o sistema irá enviar o comunicado
     *  para os mesmos
     *
     * @param frase - Texto a ser enviado
     * @param todos - Responsavel em buscar a lista de destinatarios sendo eles:
     *                  (Diretores, Coordenadores, Funcionarios, Professores, Alunos);
     * @param diretores - Filtro para buscar os destinatarios do tipo Diretor
     * @param coordenadores - Filtro para buscar os destinatarios do tipo Coordenador
     * @param funcionarios - Filtro para buscar os destinatarios do tipo Funcionario
     * @param professores - Filtro para buscar os destinatarios do tipo Professores
     * @param alunos - Filtro para buscar os destinatarios do tipo Alunos
     */
    public void executarCompartilhamentoFraseInspiracao(String frase,String autor, Boolean todos,
             Boolean cargo, Integer codigoCargo,
            Boolean departamento, Integer codigoDepartamento,
            Boolean funcionarios,
            Boolean professores,
            Boolean alunos,
            Boolean enviarEmail, UsuarioVO usuario,ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception ;

    public void criarFileCorpoMensagemEmail(ComunicacaoInternaVO obj) throws Exception;

    public void enviarEmailComunicacaoInterna(ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO config, PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, ProgressBarVO progressBarVO, boolean enviarEmailMultiplosDestinatarios) throws Exception;

    public Integer consultaRapidaComunicacaoInternaNaoLidas(Integer codUsuarioLogado) throws Exception;

    public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoProfessor(Integer codUsuarioLogado) throws Exception;

    public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoCoordenador(Integer codUsuarioLogado) throws Exception;

    public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoAluno(Integer codUsuarioLogado) throws Exception;

    public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoFuncionario(Integer codUsuarioLogado) throws Exception;

    public void alterarDataExibicaoComunicadoInterno(final Integer codigo, final Date dataExibicaoFinal, final Integer responsavelCancelamento, UsuarioVO usuarioVO) throws Exception;

    public List<ComunicacaoInternaVO> consultaRapidaComunicacaoInternaNaoLidasMenu(Integer codUsuarioLogado) throws Exception;

    public Integer consultaRapidaComunicacaoInternaLidasNaoLidas(Integer codUsuarioLogado, Boolean situacao, Date dataIni, Date dataFim) throws Exception;

    public Integer consultaRapidaComunicacaoInternaLidasNaoLidasFuncionarioOuAdministrador(Integer codUsuarioLogado, Boolean situacao, Date dataIni, Date dataFim) throws Exception;

    public void executarNotificacaoMensagemPreDefinidaTextoPadrao(TextoPadraoBancoCurriculumVO textoPadrao, PessoaVO pessoa, ParceiroVO parceiro, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public void executarNotificacaoMatriculaSerasa(NegociacaoRecebimentoVO negociacaoRecebimentoVO, PessoaVO func, String textoPadrao, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    public Integer consultaRapidaComunicacaoInternaNaoLidasVisaoPais(Integer codUsuarioLogado) throws Exception;

    public Integer consultaRapidaComunicacaoInterna(Integer codUsuarioLogado) throws Exception;

    public List<ComunicacaoInternaVO> consultaRapidaComunicacaoInterna(Integer codUsuarioLogado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void executarNotificacaoSuspensaoMatricula(ComunicacaoInternaVO comunicacaoInternaVO, PessoaVO aluno, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void preencherTodosListaTurma(List<TurmaVO> listaTurmaVOs);

	void desmarcarTodosListaTurma(List<TurmaVO> listaTurmaVOs);
	
	public Boolean realizarVerificacaoNivelEducacionalCursoTurmaSelecionada(List<TurmaVO> listaTurmaVOs, UsuarioVO usuarioVO);
	
	public String removerCabecalhoERodapeComunicadoInterno(String body) throws Exception;
	
	public void executarNotificacaoAlteracaoDataProvaCandidado(ComunicacaoInternaVO comunicacaoInternaVO,ConfiguracaoGeralSistemaVO configuracaoSistema, UsuarioVO usuario) throws Exception;
	
	public ComunicacaoInternaVO realizarMontagemNotificacaoAlteracaoDataProva(InscricaoVO inscricaoVO, ConfiguracaoGeralSistemaVO configuracaoSistema, UsuarioVO usuario, String corpoMensagemSMS, String corpoMensagem, Boolean enviarSmsInformativoAlteracaoDataProva,String assuntoEmail) throws Exception;

	void realizarTrocarLogoEmailPorUnidadeEnsino(ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoSistema, UsuarioVO usuario);

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param codigo
	 * @param tipoDestinatario
	 * @param lida
	 * @param respondida
	 * @param dataIni
	 * @param dataFim
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param limit
	 * @param offset
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	void consultaRapidaPorSituacaoEntradaLimitOffset(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param codigo
	 * @param tipoDestinatario
	 * @param lida
	 * @param respondida
	 * @param dataIni
	 * @param dataFim
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	Integer consultaQuantidadeRapidaPorSituacaoEntrada(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 21 de nov de 2016 
	 * @param comunicacaoInternaVO
	 * @param caminhoWeb
	 * @param texto
	 * @return
	 * @throws Exception 
	 */
	String redimensionarTextoHTMLAplicativo(ComunicacaoInternaVO comunicacaoInternaVO, String caminhoWeb, String texto) throws Exception;

	List<ComunicacaoInternaVO> consultarComunicacaoInternaPorAlunoFichaAluno(PessoaVO aluno, String mesAno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Integer limit, Integer offset ) throws Exception;

	List<SelectItem> consultarMesAnoComunicacaoInternaPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO);
	Integer consultaRapidaQuantidadeEntradaVisaoAluno(Integer codigo, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadeEntradaVisaoProfessor(Integer codigo, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadeEntradaVisaoCoordenador(Integer codigo, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadePorCodigoResponsavelFuncionarioOuAdministrador(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadePorCodigoResponsavel(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadePorCodigoResponsavelVisaoProfessor(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadePorCodigoResponsavelVisaoAluno(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadePorCodigoResponsavelVisaoCoordenador(Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadeSituacaoEntradaVisaoFuncionarioOuAdministrador(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadeSituacaoEntradaLimitOffset(Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	Integer consultaRapidaQuantidadeEntradaLimite(Integer codigo, String tipoDestinatario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData);
	
	public void consultaRapidaPorSituacaoEntradaLimitOffset(DataModelo controleConsulta, Integer codigo, String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario, String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception ;
	
	public void consultaRapidaPorEntradaLimiteOffsetVisaoProfessor(DataModelo controleConsulta,  Integer codigo, String tipoDestinatario, Integer limite, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer offset) throws Exception;
	
	public List<ComunicacaoInternaVO> consultarPorTipoOrigemComunicacaoInterna(Integer codigoTipoOrigemComunicacaoInterna, TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void excluirPorTipoOrigemComunicacaoInterna(Integer codigoTipoOrigemComunicacaoInterna, TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum, UsuarioVO usuario);

	public List<ComunicacaoInternaVO> consultarPorTipoOrigemECodigoTipoOrigemEPessoa(TipoOrigemComunicacaoInternaEnum tipoOrigemComunicacaoInternaEnum, Integer codigoTipoOrigem, PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception;

	Integer consultarTotalRegistroComunicacaoInternaPorAlunoFichaAluno(Integer aluno, String mesAno,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	String consultarBannerComunicadoInternoMarketingDisponivelUsuario(UsuarioVO usuario) throws Exception;
	public String substituirTag(String mensagem, PessoaVO pessoaVO) throws Exception;

	void consultaRapidaPorEntradaLimite(DataModelo controleConsulta, Integer codigo, String tipoDestinatario,
			Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,
			String filtroPorAssunto, String filtroPorRemetente, String filtroPorData, Boolean somenteNaoLidas)
			throws Exception;

	void consultaRapidaPorCodigoResponsavelFuncionarioOuAdministrador(DataModelo controleConsulta,
			Integer valorConsulta, String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto,
			String filtroPorRemetente, String filtroPorData) throws Exception;

	void consultaRapidaPorCodigoResponsavelVisaoCoordenador(DataModelo controleConsulta, Integer valorConsulta,
			String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente,
			String filtroPorData) throws Exception;

	void consultaRapidaPorCodigoResponsavelVisaoAluno(DataModelo controleConsulta, Integer valorConsulta,
			String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente,
			String filtroPorData) throws Exception;

	void consultaRapidaPorCodigoResponsavelVisaoProfessor(DataModelo controleConsulta, Integer valorConsulta,
			String tipoSaidaConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario, Integer limite, Integer pagina, String filtroPorAssunto, String filtroPorRemetente,
			String filtroPorData) throws Exception;

	void consultaRapidaPorSituacaoEntradaVisaoFuncionarioOuAdministrador(DataModelo controleConsulta, Integer codigo,
			String tipoDestinatario, Boolean lida, Boolean respondida, Date dataIni, Date dataFim,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer pagina,
			String filtroPorAssunto, String filtroPorRemetente, String filtroPorData) throws Exception;

	Integer consultaQuantidadeRapidaPorSituacaoEntradaProfessor(Integer codigo, String tipoDestinatario, Boolean lida,
			Boolean respondida, Date dataIni, Date dataFim, UsuarioVO usuario) throws Exception;

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela.
	 * Está consulta é considerada Padrão pelo motivo de todos os métodos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * cláusulas de condições e ordenação com limite e offset
	 * 
	 * @author Victor Hugo
	 */
	List<ComunicacaoInternaVO> consultaRapidaPorSituacaoEntradaLimitOffset(Integer codigo, String tipoDestinatario,
			Boolean lida, Boolean respondida, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,
			Integer limit, Integer offset, UsuarioVO usuario) throws Exception;
	
	public void preencherDestinatariosPlanilhaExcel(FileUploadEvent uploadEvent, ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ComunicacaoInternaVO comunicacaoInterna, UsuarioVO usuario) throws Exception;
	
	void incluir(final ComunicacaoInternaVO obj, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO, boolean enviarEmailMultiplosDestinatarios) throws Exception;
	
	void realizarValidacaoTamanhoAnexosEmail(UploadedFile uploadedFile, List<ArquivoVO> arquivoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
}

