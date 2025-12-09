
package negocio.comuns.arquitetura;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PreferenciaSistemaUsuarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PlataformaEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import webservice.servicos.objetos.PerfilAcessoAplicativoRSVO;

/**
 * Reponsável por manter os dados da entidade Usuario. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "usuario")
public class UsuarioVO extends SuperVO {

	 private Integer codigo;
	    private String nome;
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private String username;
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private String senha;
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private String senhaSHA;
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private String senhaSHABase64;
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private String senhaMSCHAPV2;    
	    private String matricula;
	    private PessoaVO pessoa;
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private List<UsuarioPerfilAcessoVO> usuarioPerfilAcessoVOs;
	    private String tipoUsuario;
	    private Boolean perfilAdministrador;
	    private List listaOpcoesVisoes;
	    private Boolean escolhaFuncionario;
	    private Boolean escolhaAdministrador;
	    private Boolean escolhaProfessor;
	    private Boolean escolhaAluno;
	    private Boolean opcaoVisao;
	    private String visaoLogar;
	    private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;    
	    private List<AvaliacaoInstitucionalVO> avaliacaoInstitucionalVOs;
	    private Boolean validaAlteracaoSenha;
	    //Atributo não gravado em Banco
	    private Boolean alterarSenha;
	    private Date dataUltimoAcesso;
	    private Date dataPrimeiroAcesso;
	    private String ipUltimoAcesso;
	    
	    /**
	     * criado por paulo para manter o perfil do usuario logado
	     */
	    private PerfilAcessoVO perfilAcesso = null;
	    private UnidadeEnsinoVO unidadeEnsinoLogado;
	    private String ipMaquinaLogada;
	    
	    // Campos usado para controle da turma no momento do professor responder a avaliação institucional
	    // NAO DEVE SER PERSISTIDO NA BASE.
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private TurmaVO turmaAvaliacaoInstitucionalProfessor;
	    @ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
	    private List<FavoritoVO> listaFavoritos;
	    private String senhaCriptografada;
	    public static final long serialVersionUID = 1L;
		//Campo usado para identificar qual usuario deve alterar a senha ao logar no sistema.
		private Boolean solicitarNovaSenha;
		
		//Atributo não gravado em Banco usado para o controle de simulacao de acesso do aluno
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private SimularAcessoAlunoVO simularAcessoAluno;
		private String tokenAplicativo;
		private String tokenRedefinirSenha;
		private Boolean resetouSenhaPrimeiroAcesso;	
		private PlataformaEnum celular;
		private String urlLogoUnidadeEnsinoLogado;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private List<TipoNivelEducacional> nivelEducacionalVisaoEnums;	
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private TipoNivelEducacional tipoNivelEducacionalLogado;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private Date dataUltimaAlteracao;
		
		private Boolean permitirRegistrarAulaRetroativoMobile;
		private Boolean permitirLancarNotaRetroativoMobile;
		private Boolean permiteVisualizarRelogioAssincrono;
		
		/**
		 * atributo transient utilizado para controlar se o usuário já liberou 
		 * gravar determinada entidade que estava bloqueada em funcao da competencia
		 * estar fechada.
		 */
		private Boolean usuarioJaLiberouRegistroCompetenciaFechada;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private Boolean possuiCadastroLdap;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private Boolean ativoLdap;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private Integer qtdFalhaLogin;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private Date dataFalhaLogin;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private Boolean usuarioBloqPorFalhaLogin;	
		private String nomeAplicativo;
		private String versaoAplicativo;
		private Boolean permitirAlunoVizualizarMateriaisPeriodoConcluido;
		private String periodicidadeCurso;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private Boolean selecionado;
		@ExcluirJsonAnnotation
	    @Expose(deserialize = false, serialize = false)
		private String mensagemExclusaoMinhaBiblioteca;	
		private Boolean aceitouPoliticaPrivacidadeAluno;
		private Boolean aceitouPoliticaPrivacidadeProfessor;
		private Date dataUltimoAcessoBlackboard;
		private boolean  usuarioFacilitador;
		private PermissaoAcessoMenuVO permissaoAcessoMenuVO;
		
		private List<DocumentoAssinadoVO> documentoPendenteAssinatura;	
		private Boolean permitirAlunoRecusarAssinaturaContrato;
		private Boolean logadoApp;
		private String identificadorUsuarioBlackboard;

	@XmlElement(name = "ipMaquinaLogada")
    public String getIpMaquinaLogada() {
        if (ipMaquinaLogada == null) {
            ipMaquinaLogada = "";
        }
        return ipMaquinaLogada;
    }
	
	

    



	/**
     * @param ipMaquinaLogada the ipMaquinaLogada to set
     */
    public void setIpMaquinaLogada(String ipMaquinaLogada) {
        this.ipMaquinaLogada = ipMaquinaLogada;
    }

    /**
     * Construtor padrão da classe <code>Usuario</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public UsuarioVO() {
        super();
    }

    public UnidadeEnsinoVO getUnidadeEnsinoLogado() {
        if (unidadeEnsinoLogado == null) {
            unidadeEnsinoLogado = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoLogado;
    }

    public void setUnidadeEnsinoLogado(UnidadeEnsinoVO unidadeEnsinoLogado) {
        this.unidadeEnsinoLogado = unidadeEnsinoLogado;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>UsuarioVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public static void validarDados(UsuarioVO obj) throws ConsistirException {
        if (obj.getTipoUsuario().equals("")) {
            throw new ConsistirException("O campo TIPO USUÁRIO (Usuario) deve ser informado.");
        }
        if (!obj.getTipoUsuario().equals("PA")) {
            if (!obj.getPerfilAdministrador() &&  ((obj.getPessoa() == null) || (obj.getPessoa().getCodigo().intValue() == 0))) {
                throw new ConsistirException("O campo Pessoa Referente ao Usuário deve ser informado.");
            }
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Usuário) deve ser informado.");
        }
        if (obj.getUsername().equals("")) {
            throw new ConsistirException("O campo USERNAME (Usuário) deve ser informado.");
        }
        if (obj.getSenha().equals("")) {
            throw new ConsistirException("O campo SENHA (Usuário) deve ser informado.");
        }
//        if (obj.getPerfilAcesso().getCodigo() == 0)){
//            throw new ConsistirException("Deve ser informado pelo menos um PERFIL DE ACESSO");
//        }
        // } else if (!obj.getTipoUsuario().equals("AL")) && () {
        // if (obj.getUsuarioPerfilAcessoVOs().size() == 0) {
        // throw new ConsistirException("A lista de UNIDADE ENSINO e NOME DO PERFIL ACESSO (Usuario) deve ser informado.");
        // }
        // }
    }

    public static void validarDadosAlterarSenha(UsuarioVO obj, String usernameAtual, String senhaAtual, String usernameNovo, String senhaNova) throws ConsistirException, UnsupportedEncodingException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (usernameAtual.equals("")) {
            throw new ConsistirException("O campo USUÁRIO ATUAL (Configuração) deve ser informado.");
        }
        if (senhaAtual.equals("")) {
            throw new ConsistirException("O campo SENHA ATUAL (Configuração) deve ser informado.");
        }
        if (usernameNovo.equals("")) {
            throw new ConsistirException("O campo USUÁRIO NOVO (Configuração) deve ser informado.");
        }
        if (senhaNova.equals("")) {
            throw new ConsistirException("O campo SENHA NOVA(Configuração) deve ser informado.");
        }
        if (!obj.getUsername().equals(usernameAtual) || !obj.getSenha().equals(Uteis.encriptar(senhaAtual))) {
            throw new ConsistirException("O campo USUÁRIO ATUAL ou SENHA ATUAL (Configuração) não conferem.");
        }
    }

    public void popularDadosUsuarioApartirDoFuncionario(FuncionarioVO func) {
        setPessoa(func.getPessoa());
        setTipoUsuario("FU");
    }

    public void validarConfiguracaoGeralSistema() throws Exception {
        if (getConfiguracaoGeralSistemaVO().getCodigo().intValue() == 0) {
            throw new Exception("Não Existe uma Configuração Geral do Sistemas definida.");
        }
    }

    public PerfilAcessoVO definirPerfilAcesso(String visao) throws Exception {
        if (visao.equals("aluno")) {
            validarConfiguracaoGeralSistema();
            return getConfiguracaoGeralSistemaVO().getPerfilPadraoAluno();
        }
        if (visao.equals("pais")) {
            validarConfiguracaoGeralSistema();
            return getConfiguracaoGeralSistemaVO().getPerfilPadraoPais();
        }
        if (visao.equals("professor")) {
            validarConfiguracaoGeralSistema();
            return getConfiguracaoGeralSistemaVO().getPerfilPadraoProfessorGraduacao();
        }
        if (visao.equals("candidato")) {
            validarConfiguracaoGeralSistema();
            return getConfiguracaoGeralSistemaVO().getPerfilPadraoCandidato();
        }
        return null;
    }

    public String definirVisaoLogar(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Integer unidadeEnsino, FuncionarioVO funcionario, List<MatriculaVO> matriculas) throws Exception {
        setConfiguracaoGeralSistemaVO(configuracaoGeralSistemaVO);
        setOpcaoVisao(false);
        setListaOpcoesVisoes(new ArrayList());
        String visao = "login";

        // valida se o usuario logado é
        // Candidato/Professor/Aluno/ExerceCargoAdministrativo
        if (getTipoUsuario().equals("CA")) {
            setVisaoLogar("candidato");
            return "candidato";
        }
        if (getPessoa().getProfessor() && getPessoa().getAtivo()) {
            setEscolhaProfessor(true);
            if (getPessoa().getAluno()) {
                if (!matriculas.isEmpty()) {
                    setEscolhaAluno(true);
                    setOpcaoVisao(true);
                }
            }
            if (funcionario.getExerceCargoAdministrativo()) {
                if (this.getPerfilAdministrador()) {
                    setEscolhaAdministrador(true);
                } else {
                    setEscolhaFuncionario(true);
                }
                setOpcaoVisao(true);
            }
            if (getOpcaoVisao()) {
                return "erroLogin";
            } else {
                if ((getPessoa().getValorCssTopoLogo().equals("") && getPessoa().getValorCssBackground().equals("") && getPessoa().getValorCssMenu().equals(""))) {
                    getPessoa().setValorCssTopoLogo(configuracaoGeralSistemaVO.getVisaoPadraoProfessor().getValorCssTopoLogo());
                    getPessoa().setValorCssBackground(configuracaoGeralSistemaVO.getVisaoPadraoProfessor().getValorCssBackground());
                    getPessoa().setValorCssMenu(configuracaoGeralSistemaVO.getVisaoPadraoProfessor().getValorCssMenu());
                }
                setVisaoLogar("professor");
                return "professor";
            }
        } else if (getPessoa().getFuncionario()  && getPessoa().getAtivo()) {
            setEscolhaFuncionario(true);
            if (getPessoa().getAluno()) {
                if (!matriculas.isEmpty()) {
                    setEscolhaAluno(true);
                    setOpcaoVisao(true);
                }
            }
            if (getOpcaoVisao()) {
                return "erroLogin";
            } else {
                setVisaoLogar("funcionario");
                return "funcionario";
            }
        } else if (getPessoa().getAluno()) {
            if ((getPessoa().getValorCssTopoLogo().equals("") && getPessoa().getValorCssBackground().equals("") && getPessoa().getValorCssMenu().equals(""))) {
                getPessoa().setValorCssTopoLogo(configuracaoGeralSistemaVO.getVisaoPadraoAluno().getValorCssTopoLogo());
                getPessoa().setValorCssBackground(configuracaoGeralSistemaVO.getVisaoPadraoAluno().getValorCssBackground());
                getPessoa().setValorCssMenu(configuracaoGeralSistemaVO.getVisaoPadraoAluno().getValorCssMenu());
            }
            setVisaoLogar("aluno");
            return "aluno";
        } else if (getPessoa().getCandidato()) {
            if ((getPessoa().getValorCssTopoLogo().equals(""))) {
                getPessoa().setValorCssTopoLogo(configuracaoGeralSistemaVO.getVisaoPadraoCandidato().getValorCssTopoLogo());
                getPessoa().setValorCssBackground(configuracaoGeralSistemaVO.getVisaoPadraoCandidato().getValorCssBackground());
                getPessoa().setValorCssMenu(configuracaoGeralSistemaVO.getVisaoPadraoCandidato().getValorCssMenu());
            }
            setVisaoLogar("candidato");
            return "candidato";
        } else if (getPessoa().getMembroComunidade()) {
            setVisaoLogar("login");
            return "login";
        }
        setVisaoLogar("funcionario");
        return visao;
    }

    public String getTipoPessoa() {
        if (getVisaoLogar().equals("aluno")) {
            return "AL";
        }
        if (getVisaoLogar().equals("candidato")) {
            return "CA";
        }
        if (getVisaoLogar().equals("professor")) {
            return "PR";
        }
        if (getVisaoLogar().equals("coordenador")) {
            return "CO";
        }
        if (getVisaoLogar().equals("parceiro")) {
            return "PA";
        }
        if (getVisaoLogar().equals("pais")) {
            return "RL";
        }
        return "FU";
    }

    public void adicionarObjUsuarioPerfilAcessoVOs(UsuarioPerfilAcessoVO obj) throws Exception {
        if ((getTipoUsuario() == null)) {
            throw new ConsistirException("O campo TIPO USUÁRIO (Usuario) deve ser informado.");
        }
        if (getTipoUsuario().equals("DM") || getTipoUsuario().equals("PA")) {
            UsuarioPerfilAcessoVO.validarDadosEspecial(obj);

        } else {
            if (obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
                throw new ConsistirException("Para o tipo de usuário selecionado deve ser vinculado ao PERFIL ACESSO uma UNIDADE ENSINO.");
            }
            UsuarioPerfilAcessoVO.validarDados(obj);
        }
        for (UsuarioPerfilAcessoVO objExistente : (List<UsuarioPerfilAcessoVO>) getUsuarioPerfilAcessoVOs()) {
//            if ((objExistente.getUnidadeEnsinoVO().getCodigo().intValue() == obj.getUnidadeEnsinoVO().getCodigo().intValue())) {
            if ((objExistente.getUnidadeEnsinoVO().getCodigo().intValue() == obj.getUnidadeEnsinoVO().getCodigo().intValue()) 
            		&& (objExistente.getPerfilAcesso().getCodigo().intValue() == obj.getPerfilAcesso().getCodigo().intValue())) {
                throw new ConsistirException("Este perfil de acesso já está vinculado a esta unidade de ensino.");
            }
        }
        getUsuarioPerfilAcessoVOs().add(obj);
    }

    public void adicionarObjUsuarioPerfilAcessoAPartirMatriculaVOs(UsuarioPerfilAcessoVO obj) throws Exception {
        int index = 0;
        Iterator i = getUsuarioPerfilAcessoVOs().iterator();
        while (i.hasNext()) {
            UsuarioPerfilAcessoVO objExistente = (UsuarioPerfilAcessoVO) i.next();
            if (objExistente.getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo())) {
                return;
            }
            index++;
        }
        getUsuarioPerfilAcessoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>LimiteClienteVO</code> no List <code>limiteClienteVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>LimiteCliente</code> - getValor() - como identificador (key) do objeto no List.
     *
     * @param valor
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjUsuarioPerfilAcessoVOs(UsuarioPerfilAcessoVO obj) throws Exception {
        int index = 0;
        Iterator i = getUsuarioPerfilAcessoVOs().iterator();
        while (i.hasNext()) {
            UsuarioPerfilAcessoVO objExistente = (UsuarioPerfilAcessoVO) i.next();
            if (objExistente.getPerfilAcesso().getCodigo().equals(obj.getPerfilAcesso().getCodigo()) && objExistente.getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo())) {
                getUsuarioPerfilAcessoVOs().remove(index);
                return;
            }
            index++;
        }
    }
    @XmlElement(name = "senha")
    public String getSenha() {
        if (senha == null) {
            senha = "Senha";
        }
        return (senha);
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @XmlElement(name = "username")
    public String getUsername() {
        if (username == null) {
            username = "Usuário";
        }
        return (username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement(name = "nome")
    public String getNome() {
        
        	return nome;
        
    
    }

    public String getNome_Apresentar() {
    	if(getNome().isEmpty()) {
    		return "Administrador Sistema";
    	}
        if (getNome().length() > 15) {
            return Uteis.getNomeResumidoPessoa(getNome());
        }
        return (getNome());
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "pessoa")
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipoUsuario_Apresentar() {
        return TipoUsuario.getDescricao(getTipoUsuario());
    }

    public String getPessoaConsulta() {
        if (getTipoUsuario().equals("DM") || getTipoUsuario().equals("DC") || getTipoUsuario().equals("CO") || getTipoUsuario().equals("FU")) {
            return "FU";
        }
        if (getTipoUsuario().equals("AL")) {
            return "AL";
        }
        if (getTipoUsuario().equals("PR")) {
            return "PR";
        }
        if (getTipoUsuario().equals("RL")) {
            return getTipoUsuario();
        }

        return ("");
    }

    public Boolean getExistePessoa() {
        if (getTipoUsuario() == null || getTipoUsuario().equals("VI") || getTipoUsuario().equals("PA") || getTipoUsuario().equals("")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean getApresentarListaPerfilAcesso() {
        if (getTipoUsuario() != null && !getTipoUsuario().equals("AL") && !getTipoUsuario().equals("RL")  && !getTipoUsuario().equals("CA")) {
            return true;
        }
        return false;
    }

    @XmlElement(name = "tipoUsuario")
    public String getTipoUsuario() {
        if (tipoUsuario == null) {
            tipoUsuario = "";
        }
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<UsuarioPerfilAcessoVO> getUsuarioPerfilAcessoVOs() {
        if (usuarioPerfilAcessoVOs == null) {
            usuarioPerfilAcessoVOs = new ArrayList<UsuarioPerfilAcessoVO>(0);
        }
        return usuarioPerfilAcessoVOs;
    }

    public void setUsuarioPerfilAcessoVOs(List<UsuarioPerfilAcessoVO> usuarioPerfilAcessoVOs) {
        this.usuarioPerfilAcessoVOs = usuarioPerfilAcessoVOs;
    }
    @XmlElement(name = "matricula")
    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }
    
    public void setMatricula(String matricula) {
    	this.matricula = matricula;
    }

    public Boolean getPerfilAdministrador() {
        if (perfilAdministrador == null) {
            perfilAdministrador = true;
        }
        return perfilAdministrador;
    }

    public void setPerfilAdministrador(Boolean perfilAdministrador) {
        this.perfilAdministrador = perfilAdministrador;
    }

    public List getListaOpcoesVisoes() {
        if (listaOpcoesVisoes == null) {
            listaOpcoesVisoes = new ArrayList();
        }
        return listaOpcoesVisoes;
    }

    public void setListaOpcoesVisoes(List listaOpcoesVisoes) {
        this.listaOpcoesVisoes = listaOpcoesVisoes;
    }

    public Boolean getEscolhaAluno() {
        if (escolhaAluno == null) {
            escolhaAluno = false;
        }
        return escolhaAluno;
    }

    public void setEscolhaAluno(Boolean escolhaAluno) {
        this.escolhaAluno = escolhaAluno;
    }

    public Boolean getEscolhaProfessor() {
        if (escolhaProfessor == null) {
            escolhaProfessor = false;
        }
        return escolhaProfessor;
    }

    public void setEscolhaProfessor(Boolean escolhaProfessor) {
        this.escolhaProfessor = escolhaProfessor;
    }

    public Boolean getOpcaoVisao() {
        if (opcaoVisao == null) {
            opcaoVisao = false;
        }
        return opcaoVisao;
    }

    public void setOpcaoVisao(Boolean opcaoVisao) {
        this.opcaoVisao = opcaoVisao;
    }

    public Boolean getEscolhaAdministrador() {
        if (escolhaAdministrador == null) {
            escolhaAdministrador = false;
        }
        return escolhaAdministrador;
    }

    public void setEscolhaAdministrador(Boolean escolhaAdministrador) {
        this.escolhaAdministrador = escolhaAdministrador;
    }

    public Boolean getEscolhaFuncionario() {
        if (escolhaFuncionario == null) {
            escolhaFuncionario = false;
        }
        return escolhaFuncionario;
    }

    public void setEscolhaFuncionario(Boolean escolhaFuncionario) {
        this.escolhaFuncionario = escolhaFuncionario;
    }

    public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
        if (configuracaoGeralSistemaVO == null) {
            configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
        }
        return configuracaoGeralSistemaVO;
    }

    public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
    }

    @XmlElement(name = "visaoLogar")
    public String getVisaoLogar() {
        if (visaoLogar == null) {
            visaoLogar = "";
        }
        return visaoLogar;
    }

    public void setVisaoLogar(String visaoLogar) {
        this.visaoLogar = visaoLogar;
    }

    public Boolean getIsApresentarVisaoPais() {
        if (getPessoa().getPossuiAcessoVisaoPais() && getVisaoLogar().equalsIgnoreCase("pais")) {
            return true;
        }
        return false;
    }

    public Boolean getIsApresentarVisaoProfessor() {
        return getVisaoLogar().equalsIgnoreCase("professor");
    }

    public Boolean getIsApresentarVisaoCoordenador() {
        return getVisaoLogar().equalsIgnoreCase("coordenador");
    }
    
    public Boolean getIsApresentarVisaoAdministrativa() {
        return !getIsApresentarVisaoAluno() && !getIsApresentarVisaoPais() && !getIsApresentarVisaoProfessor() && !getIsApresentarVisaoCoordenador() && !getIsApresentarVisaoOuvidoria() && !getIsApresentarVisaoParceiro();
    }

    public Boolean getIsApresentarVisaoAluno() {
        return getVisaoLogar().equalsIgnoreCase("aluno");
    }
    
    public Boolean getIsApresentarVisaoOuvidoria() {
        return getVisaoLogar().equalsIgnoreCase("ouvidoria");
    }
    
    public Boolean getIsApresentarVisaoParceiro() {
        return getVisaoLogar().equalsIgnoreCase("parceiro");
    }

    public List<AvaliacaoInstitucionalVO> getAvaliacaoInstitucionalVOs() {
        if (avaliacaoInstitucionalVOs == null) {
            avaliacaoInstitucionalVOs = new ArrayList<AvaliacaoInstitucionalVO>();
        }
        return avaliacaoInstitucionalVOs;
    }

    public void setAvaliacaoInstitucionalVOs(List<AvaliacaoInstitucionalVO> avaliacaoInstitucionalVOs) {
        this.avaliacaoInstitucionalVOs = avaliacaoInstitucionalVOs;
    }

    public Boolean getValidaAlteracaoSenha() {
        if (validaAlteracaoSenha == null) {
            validaAlteracaoSenha = false;
        }
        return validaAlteracaoSenha;
    }

    public void setValidaAlteracaoSenha(Boolean validaAlteracaoSenha) {
        this.validaAlteracaoSenha = validaAlteracaoSenha;
    }

    @Override
    public String toString() {
        return "Usuário: " + this.getCodigo() + " Nome: " + this.getNome() + " Tipo Pessoa: " + this.getTipoPessoa() + " Username: " + this.getUsername();
    }

    /**
     * @return the alterarSenha
     */
    public Boolean getAlterarSenha() {
        if (alterarSenha == null) {
            alterarSenha = Boolean.FALSE;
        }
        return alterarSenha;
    }

    /**
     * @param alterarSenha the alterarSenha to set
     */
    public void setAlterarSenha(Boolean alterarSenha) {
        this.alterarSenha = alterarSenha;
    }

    public Date getDataUltimoAcessoBlackboard() {
		return dataUltimoAcessoBlackboard;
	}

	public void setDataUltimoAcessoBlackboard(Date dataUltimoAcessoBlackboard) {
		this.dataUltimoAcessoBlackboard = dataUltimoAcessoBlackboard;
	}

	/**
     * @return the dataUltimoAcesso
     */
    public Date getDataUltimoAcesso() {
        if (dataUltimoAcesso == null) {
            return new Date();
        }
        return dataUltimoAcesso;
    }

    /**
     * @param dataUltimoAcesso the dataUltimoAcesso to set
     */
    public void setDataUltimoAcesso(Date dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }

    @XmlElement(name = "perfilAcesso")
    public PerfilAcessoVO getPerfilAcesso() {
        if (perfilAcesso == null) {
            perfilAcesso = new PerfilAcessoVO();
        }
        return perfilAcesso;
    }

    public void setPerfilAcesso(PerfilAcessoVO perfilAcesso) {
        this.perfilAcesso = perfilAcesso;
    }

 

    public Boolean getTipoParcerio() {
        if (getTipoUsuario().equals("PA")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getTipoOuvidoria() {
        if (getTipoUsuario().equals("OU")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @return the turmaAvaliacaoInstitucionalProfessor
     */
    public TurmaVO getTurmaAvaliacaoInstitucionalProfessor() {
        return turmaAvaliacaoInstitucionalProfessor;
    }

    /**
     * @param turmaAvaliacaoInstitucionalProfessor the turmaAvaliacaoInstitucionalProfessor to set
     */
    public void setTurmaAvaliacaoInstitucionalProfessor(TurmaVO turmaAvaliacaoInstitucionalProfessor) {
        this.turmaAvaliacaoInstitucionalProfessor = turmaAvaliacaoInstitucionalProfessor;
    }

    /**
     * @return the dataPrimeiroAcesso
     */
    public Date getDataPrimeiroAcesso() {
        return dataPrimeiroAcesso;
    }

    /**
     * @param dataPrimeiroAcesso the dataPrimeiroAcesso to set
     */
    public void setDataPrimeiroAcesso(Date dataPrimeiroAcesso) {
        this.dataPrimeiroAcesso = dataPrimeiroAcesso;
    }

    public List montarMenuFavorito(PermissaoAcessoMenuVO permissaoAcessoMenuVO, List<FavoritoVO> favoritos) {
        List listaFavoritos = new ArrayList();
        for (FavoritoVO favoritoVO : favoritos) {
            try {
                Class permissaoMenu = Class.forName(permissaoAcessoMenuVO.getClass().getName());

                Method metodoGet = permissaoMenu.getMethod("get" + favoritoVO.getBooleanMarcarFavoritar().replace("Favorito", ""));
                Boolean valor = (Boolean)metodoGet.invoke(permissaoAcessoMenuVO);
                if (valor) {
                    Method metodoSet = permissaoMenu.getMethod("set" + favoritoVO.getBooleanMarcarFavoritar(), Boolean.class);
                    metodoSet.invoke(permissaoAcessoMenuVO, Boolean.TRUE);
                    listaFavoritos.add(favoritoVO);
                }
            } catch (Exception e) {
               ////System.out.print(e.getMessage());
            }
        }
        return listaFavoritos;
    }

    /**
     * @return the listaFavoritos
     */
    public List<FavoritoVO> getListaFavoritos() {
        if (listaFavoritos == null) {
            listaFavoritos = new ArrayList<FavoritoVO>();
        }
        return listaFavoritos;
    }

    /**
     * @param listaFavoritos the listaFavoritos to set
     */
    public void setListaFavoritos(List<FavoritoVO> listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }

	public String getSenhaCriptografada() {
		if (senhaCriptografada == null) {
			senhaCriptografada = getSenha();
		}
		return senhaCriptografada;
	}

	public void setSenhaCriptografada(String senhaCriptografada) {
		this.senhaCriptografada = senhaCriptografada;
	}

	public Boolean getSolicitarNovaSenha() {
		if (solicitarNovaSenha == null) {
			solicitarNovaSenha = Boolean.FALSE;
		}
		return solicitarNovaSenha;
	}

	public void setSolicitarNovaSenha(Boolean solicitarNovaSenha) {
		this.solicitarNovaSenha = solicitarNovaSenha;
	}
	
	public TipoVisaoEnum getTipoVisaoAcesso(){
		if(getIsApresentarVisaoAdministrativa()){
			return TipoVisaoEnum.ADMINISTRATIVA;
		}
		if(getIsApresentarVisaoAluno()){
			return TipoVisaoEnum.ALUNO;
		}
		if(getIsApresentarVisaoCoordenador()){
			return TipoVisaoEnum.COORDENADOR;
		}
		if(getIsApresentarVisaoProfessor()){
			return TipoVisaoEnum.PROFESSOR;
		}
		if(getIsApresentarVisaoPais()){
			return TipoVisaoEnum.PAIS;
		}
		return null;
	}
	
	public Boolean getPermiteSimularNavegacaoAluno() {
		return Uteis.isAtributoPreenchido(getSimularAcessoAluno());
	}	

	public Boolean getSimulacaoValida() {
		if(Uteis.isAtributoPreenchido(getSimularAcessoAluno())){
			return true;
		}else
			return false;
	}
	
	public SimularAcessoAlunoVO getSimularAcessoAluno() {
		if(simularAcessoAluno == null){
			simularAcessoAluno = new SimularAcessoAlunoVO();
		}
		return simularAcessoAluno;
	}

	public void setSimularAcessoAluno(SimularAcessoAlunoVO simularAcessoAluno) {
		this.simularAcessoAluno = simularAcessoAluno;
	}
	
	//Transient
	private List<MatriculaVO> matriculaVOs;

	@XmlElement(name = "matriculaVOs")
	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<MatriculaVO>();
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}
	
	//Transient
	private List<PerfilAcessoAplicativoRSVO> perfilAcessoAplicativo;
	private Integer codigoCursoLogado;
	private String nomeCursoLogado;
	private String urlFotoAluno;
	private Integer codigoUnidadeEnsinoMatriculaLogado;
	private String caminhoFotoUsuarioAplicativo;
	private String tipoNivelEducacional;

//		@XmlElement(name = "perfilAcessoAlunoAplicativo")
//		public List<PerfilAcessoAplicativoRSVO> getPerfilAcessoAplicativo() {
//			if (perfilAcessoAplicativo == null) {
//				perfilAcessoAplicativo = new ArrayList<PerfilAcessoAplicativoRSVO>();
//			}
//			return perfilAcessoAplicativo;
//		}
//
//		public void setPerfilAcessoAplicativo(List<PerfilAcessoAplicativoRSVO> perfilAcessoAlunoAplicativo) {
//			this.perfilAcessoAplicativo = perfilAcessoAlunoAplicativo;
//		}

	@XmlElement(name = "codigoCursoLogado")
	public Integer getCodigoCursoLogado() {
		if (codigoCursoLogado == null) {
			codigoCursoLogado = 0;
		}
		return codigoCursoLogado;
	}

	public void setCodigoCursoLogado(Integer codigoCursoLogado) {
		this.codigoCursoLogado = codigoCursoLogado;
	}

	@XmlElement(name = "nomeCursoLogado")
	public String getNomeCursoLogado() {
		if (nomeCursoLogado == null) {
			nomeCursoLogado = "";
		}
		return nomeCursoLogado;
	}

	public void setNomeCursoLogado(String nomeCursoLogado) {
		this.nomeCursoLogado = nomeCursoLogado;
	}

	@XmlElement(name = "urlFotoAluno")
	public String getUrlFotoAluno() {
		if (urlFotoAluno == null) {
			urlFotoAluno = "";
		}
		return urlFotoAluno;
	}

	public void setUrlFotoAluno(String urlFotoAluno) {
		this.urlFotoAluno = urlFotoAluno;
	}

	@XmlElement(name = "codigoUnidadeEnsinoMatriculaLogado")
	public Integer getCodigoUnidadeEnsinoMatriculaLogado() {
		if (codigoUnidadeEnsinoMatriculaLogado == null) {
			codigoUnidadeEnsinoMatriculaLogado = 0;
		}
		return codigoUnidadeEnsinoMatriculaLogado;
	}

	public void setCodigoUnidadeEnsinoMatriculaLogado(Integer codigoUnidadeEnsinoMatriculaLogado) {
		this.codigoUnidadeEnsinoMatriculaLogado = codigoUnidadeEnsinoMatriculaLogado;
	}

	@XmlElement(name = "caminhoFotoUsuarioAplicativo")
	public String getCaminhoFotoUsuarioAplicativo() {
		if (caminhoFotoUsuarioAplicativo == null) {
			caminhoFotoUsuarioAplicativo = "";
		}
		return caminhoFotoUsuarioAplicativo;
	}

	public void setCaminhoFotoUsuarioAplicativo(String caminhoFotoUsuarioAplicativo) {
		this.caminhoFotoUsuarioAplicativo = caminhoFotoUsuarioAplicativo;
	}

	@XmlElement(name = "tipoNivelEducacional")
	public String getTipoNivelEducacional() {
		if (tipoNivelEducacional == null) {
			tipoNivelEducacional = "";
		}
		return tipoNivelEducacional;
	}

	public void setTipoNivelEducacional(String tipoNivelEducacional) {
		this.tipoNivelEducacional = tipoNivelEducacional;
	}

	public String getTokenAplicativo() {
		if (tokenAplicativo == null) {
			tokenAplicativo = "";
		}
		return tokenAplicativo;
	}

	public void setTokenAplicativo(String tokenAplicativo) {
		this.tokenAplicativo = tokenAplicativo;
	}

		public PlataformaEnum getCelular() {
			if (celular == null) {
				celular = PlataformaEnum.android;
			}
			return celular;
		}

	public void setCelular(PlataformaEnum celular) {
		this.celular = celular;
	}
	
	public String getTokenRedefinirSenha() {
		if (tokenRedefinirSenha == null) {
			tokenRedefinirSenha = "";
		}
		return tokenRedefinirSenha;
	}

	public void setTokenRedefinirSenha(String tokenRedefinirSenha) {
		this.tokenRedefinirSenha = tokenRedefinirSenha;
	}

	public Boolean getResetouSenhaPrimeiroAcesso() {
		if (resetouSenhaPrimeiroAcesso == null) {
			resetouSenhaPrimeiroAcesso = Boolean.FALSE;
		}
		return resetouSenhaPrimeiroAcesso;
	}

	public void setResetouSenhaPrimeiroAcesso(Boolean resetouSenhaPrimeiroAcesso) {
		this.resetouSenhaPrimeiroAcesso = resetouSenhaPrimeiroAcesso;
	}

	@XmlElement(name = "urlLogoUnidadeEnsinoLogadoLogado")
	public String getUrlLogoUnidadeEnsinoLogado() {
		if(urlLogoUnidadeEnsinoLogado == null){
			urlLogoUnidadeEnsinoLogado = "";
		}
		return urlLogoUnidadeEnsinoLogado;
	}

	public void setUrlLogoUnidadeEnsinoLogado(String urlLogoUnidadeEnsinoLogado) {
		this.urlLogoUnidadeEnsinoLogado = urlLogoUnidadeEnsinoLogado;
	}

	public List<TipoNivelEducacional> getNivelEducacionalVisaoEnums() {
		if(nivelEducacionalVisaoEnums == null) {
			nivelEducacionalVisaoEnums =  new ArrayList<TipoNivelEducacional>(0);
		}
		return nivelEducacionalVisaoEnums;
	}

	public void setNivelEducacionalProfessorEnums(List<TipoNivelEducacional> nivelEducacionalProfessorEnums) {
		this.nivelEducacionalVisaoEnums = nivelEducacionalProfessorEnums;
	}

	public TipoNivelEducacional getTipoNivelEducacionalLogado() {
		return tipoNivelEducacionalLogado;
	}

	public void setTipoNivelEducacionalLogado(TipoNivelEducacional tipoNivelEducacionalLogado) {
		this.tipoNivelEducacionalLogado = tipoNivelEducacionalLogado;
	}

		public Date getDataUltimaAlteracao() {
			if (dataUltimaAlteracao == null)
				dataUltimaAlteracao = new Date();
			return dataUltimaAlteracao;
		}

		public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
			this.dataUltimaAlteracao = dataUltimaAlteracao;
		}
		
		/**
		 * @return the permitirRegistrarAulaRetroativoMobile
		 */
		@XmlElement(name = "permitirRegistrarAulaRetroativoMobile")
		public Boolean getPermitirRegistrarAulaRetroativoMobile() {
			return permitirRegistrarAulaRetroativoMobile;
		}

		/**
		 * @param permitirRegistrarAulaRetroativoMobile the permitirRegistrarAulaRetroativoMobile to set
		 */
		public void setPermitirRegistrarAulaRetroativoMobile(Boolean permitirRegistrarAulaRetroativoMobile) {
			this.permitirRegistrarAulaRetroativoMobile = permitirRegistrarAulaRetroativoMobile;
		}

		/**
		 * @return the permitirLancarNotaRetroativoMobile
		 */
		@XmlElement(name = "permitirLancarNotaRetroativoMobile")
		public Boolean getPermitirLancarNotaRetroativoMobile() {
			return permitirLancarNotaRetroativoMobile;
		}

		/**
		 * @param permitirLancarNotaRetroativoMobile the permitirLancarNotaRetroativoMobile to set
		 */
		public void setPermitirLancarNotaRetroativoMobile(Boolean permitirLancarNotaRetroativoMobile) {
			this.permitirLancarNotaRetroativoMobile = permitirLancarNotaRetroativoMobile;
		}
	
	public Boolean getUsuarioJaLiberouRegistroCompetenciaFechada() {
		if (usuarioJaLiberouRegistroCompetenciaFechada == null) { 
			usuarioJaLiberouRegistroCompetenciaFechada = Boolean.FALSE;
		}
		return usuarioJaLiberouRegistroCompetenciaFechada;
	}

	public void setUsuarioJaLiberouRegistroCompetenciaFechada(Boolean usuarioJaLiberouRegistroCompetenciaFechada) {
		this.usuarioJaLiberouRegistroCompetenciaFechada = usuarioJaLiberouRegistroCompetenciaFechada;
	}
	
	public Boolean getPossuiCadastroLdap() {
		if (possuiCadastroLdap == null) {
			possuiCadastroLdap = false;
		}
		return possuiCadastroLdap;
	}
	
	public void setPossuiCadastroLdap(Boolean possuiCadastroLdap) {
		this.possuiCadastroLdap = possuiCadastroLdap;
	}

	public Boolean getAtivoLdap() {
		if (ativoLdap == null) {
			ativoLdap = false;
		}
		return ativoLdap;
	}

	public void setAtivoLdap(Boolean ativoLdap) {
		this.ativoLdap = ativoLdap;
	}
	
	public Boolean getIsApresentarVisaoAlunoOuPais() {
		return getIsApresentarVisaoAluno() || getIsApresentarVisaoPais();
	}

	public Integer getQtdFalhaLogin() {
		if (qtdFalhaLogin == null) {
			qtdFalhaLogin = 0;
		}
		return qtdFalhaLogin;
	}

	public void setQtdFalhaLogin(Integer qtdFalhaLogin) {
		this.qtdFalhaLogin = qtdFalhaLogin;
	}
    //TODO atualizar
	public Date getDataFalhaLogin() {
		if(dataFalhaLogin == null) {			
			dataFalhaLogin  =new Date();
		}
		return dataFalhaLogin;
	}

	public void setDataFalhaLogin(Date dataFalhaLogin) {
		this.dataFalhaLogin = dataFalhaLogin;
	}
     //TODO atualizar
	public Boolean getUsuarioBloqPorFalhaLogin() {
		if(usuarioBloqPorFalhaLogin == null) {
			usuarioBloqPorFalhaLogin = false;
		}
		return usuarioBloqPorFalhaLogin;
	}

	public void setUsuarioBloqPorFalhaLogin(Boolean usuarioBloqPorFalhaLogin) {
		this.usuarioBloqPorFalhaLogin = usuarioBloqPorFalhaLogin;
	}

	public String getSenhaSHA() {
		if(senhaSHA == null) {
			senhaSHA = "";
		}
		return senhaSHA;
	}

	public void setSenhaSHA(String senhaSHA) {
		this.senhaSHA = senhaSHA;
	}

	public String getSenhaMSCHAPV2() {
		if(senhaMSCHAPV2 == null) {
			senhaMSCHAPV2 = "";
		}
		return senhaMSCHAPV2;
	}

	public void setSenhaMSCHAPV2(String senhaMSCHAPV2) {
		this.senhaMSCHAPV2 = senhaMSCHAPV2;
	}
	 
	
	

	public Boolean getPermiteVisualizarRelogioAssincrono() {
		if(permiteVisualizarRelogioAssincrono == null) {
			permiteVisualizarRelogioAssincrono = true;
		}
		return permiteVisualizarRelogioAssincrono;
	}

	public void setPermiteVisualizarRelogioAssincrono(Boolean permiteVisualizarRelogioAssincrono) {
		this.permiteVisualizarRelogioAssincrono = permiteVisualizarRelogioAssincrono;
	}

	public String getIpUltimoAcesso() {
		if (ipUltimoAcesso == null) {
			ipUltimoAcesso = "";
		}
		return ipUltimoAcesso;
	}

	public void setIpUltimoAcesso(String ipUltimoAcesso) {
		this.ipUltimoAcesso = ipUltimoAcesso;
	}
	
	//transiente
	private PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO;

	public PreferenciaSistemaUsuarioVO getPreferenciaSistemaUsuarioVO() {
		if(preferenciaSistemaUsuarioVO == null) {
			preferenciaSistemaUsuarioVO = new PreferenciaSistemaUsuarioVO();
		}
		return preferenciaSistemaUsuarioVO;
	}

	public void setPreferenciaSistemaUsuarioVO(PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO) {
		this.preferenciaSistemaUsuarioVO = preferenciaSistemaUsuarioVO;
	}
	
	public boolean isUsuarioFacilitador() {		
		return usuarioFacilitador;
	}

	public void setUsuarioFacilitador(boolean usuarioFacilitador) {
		this.usuarioFacilitador = usuarioFacilitador;
	}

	@XmlElement(name = "nomeAplicativo")
	public String getNomeAplicativo() {
		if(nomeAplicativo == null) {
			nomeAplicativo = "";
		}
		return nomeAplicativo;
	}

	public void setNomeAplicativo(String nomeAplicativo) {
		this.nomeAplicativo = nomeAplicativo;
	}

	@XmlElement(name = "versaoAplicativo")
	public String getVersaoAplicativo() {
		if(versaoAplicativo == null) {
			versaoAplicativo = "";
		}
		return versaoAplicativo;
	}

	public void setVersaoAplicativo(String versaoAplicativo) {
		this.versaoAplicativo = versaoAplicativo;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	@XmlElement(name = "permitirAlunoVizualizarMateriaisPeriodoConcluido")
	public Boolean getPermitirAlunoVizualizarMateriaisPeriodoConcluido() {
		if (permitirAlunoVizualizarMateriaisPeriodoConcluido == null) {
			permitirAlunoVizualizarMateriaisPeriodoConcluido = false;
		}
		return permitirAlunoVizualizarMateriaisPeriodoConcluido;
	}

	public void setPermitirAlunoVizualizarMateriaisPeriodoConcluido(
			Boolean permitirAlunoVizualizarMateriaisPeriodoConcluido) {
		this.permitirAlunoVizualizarMateriaisPeriodoConcluido = permitirAlunoVizualizarMateriaisPeriodoConcluido;
	}

	@XmlElement(name = "periodicidadeCurso")
	public String getPeriodicidadeCurso() {
		return periodicidadeCurso;
	}

	public void setPeriodicidadeCurso(String periodicidadeCurso) {
		this.periodicidadeCurso = periodicidadeCurso;
	}

	public String getMensagemExclusaoMinhaBiblioteca() {
		if (mensagemExclusaoMinhaBiblioteca == null) {
			mensagemExclusaoMinhaBiblioteca = "";
		}
		return mensagemExclusaoMinhaBiblioteca;
	}

	public void setMensagemExclusaoMinhaBiblioteca(String mensagemExclusaoMinhaBiblioteca) {
		this.mensagemExclusaoMinhaBiblioteca = mensagemExclusaoMinhaBiblioteca;
	}

	@XmlElement(name = "aceitouPoliticaPrivacidadeAluno")
	public Boolean getAceitouPoliticaPrivacidadeAluno() {
		if (aceitouPoliticaPrivacidadeAluno == null) {
			aceitouPoliticaPrivacidadeAluno = false;
		}
		return aceitouPoliticaPrivacidadeAluno;
	}

	public void setAceitouPoliticaPrivacidadeAluno(Boolean aceitouPoliticaPrivacidadeAluno) {
		this.aceitouPoliticaPrivacidadeAluno = aceitouPoliticaPrivacidadeAluno;
	}

	@XmlElement(name = "aceitouPoliticaPrivacidadeProfessor")
	public Boolean getAceitouPoliticaPrivacidadeProfessor() {
		if (aceitouPoliticaPrivacidadeProfessor == null) {
			aceitouPoliticaPrivacidadeProfessor = false;
		}
		return aceitouPoliticaPrivacidadeProfessor;
	}

	public void setAceitouPoliticaPrivacidadeProfessor(Boolean aceitouPoliticaPrivacidadeProfessor) {
		this.aceitouPoliticaPrivacidadeProfessor = aceitouPoliticaPrivacidadeProfessor;
	}

	public String getSenhaSHABase64() {
		if(senhaSHABase64 == null) {
			senhaSHABase64 =  "";
		}
		return senhaSHABase64;
	}

	public void setSenhaSHABase64(String senhaSHABase64) {
		this.senhaSHABase64 = senhaSHABase64;
	}


	@XmlElement(name = "permissaoAcessoMenu")
	public PermissaoAcessoMenuVO getPermissaoAcessoMenuVO() {
		if(permissaoAcessoMenuVO == null) {
			permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
		}
		return permissaoAcessoMenuVO;
	}

	public void setPermissaoAcessoMenuVO(PermissaoAcessoMenuVO permissaoAcessoMenuVO) {
		this.permissaoAcessoMenuVO = permissaoAcessoMenuVO;
	}

	public List<DocumentoAssinadoVO> getDocumentoPendenteAssinatura() {
		if(documentoPendenteAssinatura == null) {
			documentoPendenteAssinatura=  new ArrayList<DocumentoAssinadoVO>(0);
		}
		return documentoPendenteAssinatura;
	}

	public void setDocumentoPendenteAssinatura(List<DocumentoAssinadoVO> documentoPendenteAssinatura) {
		this.documentoPendenteAssinatura = documentoPendenteAssinatura;
	}

	@XmlElement(name = "permitirAlunoRecusarAssinaturaContrato")
	public Boolean getPermitirAlunoRecusarAssinaturaContrato() {
		if(permitirAlunoRecusarAssinaturaContrato == null) {
			permitirAlunoRecusarAssinaturaContrato =  false;
		}
		return permitirAlunoRecusarAssinaturaContrato;
	}

	public void setPermitirAlunoRecusarAssinaturaContrato(Boolean permitirAlunoRecusarAssinaturaContrato) {
		this.permitirAlunoRecusarAssinaturaContrato = permitirAlunoRecusarAssinaturaContrato;
	}

	public Boolean getLogadoApp() {
		if(logadoApp == null) {
			logadoApp =  false;
		}
		return logadoApp;
	}

	public void setLogadoApp(Boolean logadoApp) {
		this.logadoApp = logadoApp;
	}
	
	public boolean isUsuarioOtimize() {
		return getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor()) && getCodigo().equals(1);
	}
	
	public void setIdentificadorUsuarioBlackboard(String identificadorUsuarioBlackboard) {
		this.identificadorUsuarioBlackboard = identificadorUsuarioBlackboard;
	}
}