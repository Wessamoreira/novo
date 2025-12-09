package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.crm.enumerador.FormacaoAcademicaProspectEnum;
import negocio.comuns.crm.enumerador.RendaProspectEnum;
import negocio.comuns.crm.enumerador.TipoEmpresaProspectEnum;
import negocio.comuns.crm.enumerador.TipoOrigemCadastroProspectEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.segmentacao.ProspectSegmentacaoOpcaoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Prospects. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@SuppressWarnings("serial")
public class ProspectsVO extends SuperVO implements PossuiEndereco {

	private Integer codigo;
	private String nome;
	private String razaoSocial;
	private String cpf;
	private String rg;
	private String orgaoEmissor;
	private String estadoEmissor;
	private Date dataNascimento;
	private String sexo;
	private String cnpj;
	private String inscricaoEstadual;
	private String cep;
	private String endereco;
	private String complemento;
	private String setor;
	private String telefoneComercial;
	private String telefoneResidencial;
	private String telefoneRecado;
	private String celular;
	private String skype;
	private String emailPrincipal;
	private String emailSecundario;
	private RendaProspectEnum renda = RendaProspectEnum.NENHUM;
	private FormacaoAcademicaProspectEnum formacaoAcademica = FormacaoAcademicaProspectEnum.NENHUM;
	private TipoEmpresaProspectEnum tipoEmpresa = TipoEmpresaProspectEnum.NENHUM;
	private TipoProspectEnum tipoProspect = TipoProspectEnum.FISICO;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>CursoInteresse</code>.
	 */
	private List<CursoInteresseVO> cursoInteresseVOs;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private UnidadeEnsinoVO unidadeEnsino;
	private PessoaVO pessoa;
	private ArquivoVO arquivoFoto;
	private CidadeVO cidade;
	private ParceiroVO parceiro;
	private FornecedorVO fornecedor;
	private UnidadeEnsinoVO unidadeEnsinoProspect;
	private List<TipoProspectVO> listaTipoProspectVO;
	private List<RegistroEntradaVO> listaRegistroEntrada;
	private String dataNascimentoRel;
	private String nomeEmpresa;
	private String telefoneEmpresa;
	private String cargo;
	private String curso;

	private Date dataCadastro;

	private UsuarioVO responsavelCadastro;
	private TipoOrigemCadastroProspectEnum tipoOrigemCadastro;
	private Boolean inativo;
	private String motivoInativacao;
	private UsuarioVO responsavelInativacao;

	private FuncionarioVO consultorPadrao;

	private String numero;
	private String estadoCivil;
	private PaizVO nacionalidade;
	private CidadeVO naturalidade;
	private Date dataExpedicao;
	private String nomePai;
	private String nomeMae;
	private Boolean participaBancoCurriculum;
	private Boolean divulgarMeusDados;
	private List<FormacaoAcademicaVO> formacaoAcademicaVOs;

	private Date dataAlteracao;
	private UsuarioVO usuarioAlteracao;	

	/**
	 * Campo transiente NAO É PERSISTIDO E UTILIZADO SOMENTE PARA APRESENTACAO
	 * DE DADOS
	 */
	private PreInscricaoVO preInscricao;

	/*
	 * Atributo Transiente utilizado pelo registro de entrada
	 */
	private HistoricoFollowUpVO historicoFollowUp;

	private List<ProspectSegmentacaoOpcaoVO> prospectSegmentacaoOpcaoVOs;
        private TipoMidiaCaptacaoVO tipoMidia;


        /**
         * Temporarios para controle de alteracao do consultro
         */
     private Boolean consultorAlterado;
     private String acaoCompromissoAguardandoExcecucao;
     private Boolean finalizarCompromissoParalizado;
     private Boolean excluirCompromissoNaoIniciadoPassado;
     
     /**
      * Atributo Transient
      */
     private FuncionarioVO consultorUltimaInteracao;
     private FuncionarioVO consultorSugerido;
     private Boolean responsavelFinanceiro;
     

     private Boolean sincronizadoRDStation;
     private String logSincronizacaoRD;
     
     private String nomeBatismo;
     

	/**
	 * Construtor padrão da classe <code>Prospects</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ProspectsVO() {
		super();
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Prospects</code>).
	 */
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return (pessoa);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Prospects</code>).
	 */
	public void setPessoa(PessoaVO obj) {
		this.pessoa = obj;
	}

	/**
	 * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
	 * <code>Prospects</code>).
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return (unidadeEnsino);
	}

	/**
	 * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
	 * <code>Prospects</code>).
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
		this.unidadeEnsino = obj;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>CursoInteresse</code>.
	 */
	public List<CursoInteresseVO> getCursoInteresseVOs() {
		if (cursoInteresseVOs == null) {
			cursoInteresseVOs = new ArrayList(0);
		}
		return (cursoInteresseVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>CursoInteresse</code>.
	 */
	public void setCursoInteresseVOs(List<CursoInteresseVO> cursoInteresseVOs) {
		this.cursoInteresseVOs = cursoInteresseVOs;
	}

	public TipoProspectEnum getTipoProspect() {
		if (tipoProspect == null) {
			tipoProspect = TipoProspectEnum.FISICO;
		}
		return (tipoProspect);
	}

	public String getTipoProspect_Apresentar() {
		if (getTipoProspect() == TipoProspectEnum.FISICO) {
			return "Física";
		} else if (getTipoProspect() == TipoProspectEnum.JURIDICO) {
			return "Jurídica";
		}
		return "";
	}

	public void setTipoProspect(TipoProspectEnum tipoProspect) {
		if (tipoProspect == null) {
			tipoProspect = TipoProspectEnum.NENHUM;
		}
		this.tipoProspect = tipoProspect;
	}

	public TipoEmpresaProspectEnum getTipoEmpresa() {
		return (tipoEmpresa);
	}

	public void setTipoEmpresa(TipoEmpresaProspectEnum tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public FormacaoAcademicaProspectEnum getFormacaoAcademica() {
		return (formacaoAcademica);
	}

	public void setFormacaoAcademica(FormacaoAcademicaProspectEnum formacaoAcademica) {
		this.formacaoAcademica = formacaoAcademica;
	}

	public RendaProspectEnum getRenda() {
		return (renda);
	}

	public void setRenda(RendaProspectEnum renda) {
		this.renda = renda;
	}

	public String getEmailSecundario() {
		if (emailSecundario == null) {
			emailSecundario = "";
		}
		return (emailSecundario);
	}

	public void setEmailSecundario(String emailSecundario) {
		this.emailSecundario = emailSecundario;
	}

	public String getEmailPrincipal() {
		if (emailPrincipal == null) {
			emailPrincipal = "";
		}
		return (emailPrincipal);
	}

	public void setEmailPrincipal(String emailPrincipal) {
		this.emailPrincipal = emailPrincipal;
	}

	public String getSkype() {
		if (skype == null) {
			skype = "";
		}
		return (skype);
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return (celular);
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefoneRecado() {
		if (telefoneRecado == null) {
			telefoneRecado = "";
		}
		return (telefoneRecado);
	}

	public void setTelefoneRecado(String telefoneRecado) {
		this.telefoneRecado = telefoneRecado;
	}

	public String getTelefoneResidencial() {
		if (telefoneResidencial == null) {
			telefoneResidencial = "";
		}
		return (telefoneResidencial);
	}

	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}

	public String getTelefoneComercial() {
		if (telefoneComercial == null) {
			telefoneComercial = "";
		}
		return (telefoneComercial);
	}

	public void setTelefoneComercial(String telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
	}

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return (setor);
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return (complemento);
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return (endereco);
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCEP() {
		/*if (getCep() == null) {
			setCep("");
		}
		return (getCep());*/
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCEP(String cep) {
		/*this.setCep(cep);*/
		this.cep = cep;
	}

	public String getInscricaoEstadual() {
		if (inscricaoEstadual == null) {
			inscricaoEstadual = "";
		}
		return (inscricaoEstadual);
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getCnpj() {
		if (cnpj == null) {
			cnpj = "";
		}
		return (cnpj);
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getSexo() {
		if (sexo == null) {
			sexo = "";
		}
		return (sexo);
	}

	public String getSexo_Apresentar() {
		if (getSexo() == null) {
			setSexo("");
		} else if (getSexo().equals("F")) {
			return "Feminino";
		} else {
			return "Masculino";
		}
		return getSexo();
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Date getDataNascimento() {
		return (dataNascimento);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataNascimento_Apresentar() {
		return (Uteis.getData(getDataNascimento()));
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getRg() {
		if (rg == null) {
			rg = "";
		}
		return (rg);
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return (cpf);
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRazaoSocial() {
		if (razaoSocial == null) {
			razaoSocial = "";
		}
		return (razaoSocial);
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		} else {
            if (nome.contains("''")) {
        		nome = nome.replaceAll("''", "'");
            }        
        }
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Boolean getJuridico() {
		if (getTipoProspect().equals(TipoProspectEnum.JURIDICO)) {
			return true;
		}
		return false;
	}

	public Boolean getFisico() {
		if (getTipoProspect().equals(TipoProspectEnum.FISICO)) {
			return true;
		}
		return false;
	}

	public ArquivoVO getArquivoFoto() {
		if (arquivoFoto == null) {
			arquivoFoto = new ArquivoVO();
		}
		return arquivoFoto;
	}

	public void setArquivoFoto(ArquivoVO arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public Boolean getExisteImagem() {
		return !getArquivoFoto().getNome().equals("");
	}

	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Boolean getIsParceiro() {
		if (getParceiro() == null || getParceiro().getCodigo() == null || getParceiro().getCodigo() == 0) {
			return false;
		}
		return true;
	}

	public Boolean getIsUnidadeEnsino() {
		if (getUnidadeEnsinoProspect() == null || getUnidadeEnsinoProspect().getCodigo() == null || getUnidadeEnsinoProspect().getCodigo() == 0) {
			return false;
		}
		return true;
	}

	public Boolean getIsFornecedor() {
		if (getFornecedor() == null || getFornecedor().getCodigo() == null || getFornecedor().getCodigo() == 0) {
			return false;
		}
		return true;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoProspect() {
		if (unidadeEnsinoProspect == null) {
			unidadeEnsinoProspect = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoProspect;
	}

	public void setUnidadeEnsinoProspect(UnidadeEnsinoVO unidadeEnsinoProspect) {
		this.unidadeEnsinoProspect = unidadeEnsinoProspect;
	}

	public List<TipoProspectVO> getListaTipoProspectVO() {
		if (listaTipoProspectVO == null) {
			listaTipoProspectVO = new ArrayList(0);
		}
		return listaTipoProspectVO;
	}

	public void setListaTipoProspectVO(List<TipoProspectVO> listaTipoProspectVO) {
		this.setListaTipoProspectVO(listaTipoProspectVO);
	}

	public List<RegistroEntradaVO> getListaRegistroEntrada() {
		if (listaRegistroEntrada == null) {
			listaRegistroEntrada = new ArrayList(0);
		}
		return listaRegistroEntrada;
	}

	public void setListaRegistroEntrada(List<RegistroEntradaVO> listaRegistroEntrada) {
		this.setListaRegistroEntrada(listaRegistroEntrada);
	}

	public String getDataNascimentoRel() {
		if (dataNascimentoRel == null) {
			dataNascimentoRel = "";
		}
		return dataNascimentoRel;
	}

	public void setDataNascimentoRel(String dataNascimentoRel) {
		this.dataNascimentoRel = dataNascimentoRel;
	}

	public void preencherDadosPessoaComProspect() {
		this.getPessoa().setNome(this.getNome());
		this.getPessoa().setCEP(this.getCEP());
		this.getPessoa().setCPF(this.getCpf());
		this.getPessoa().setComplemento(this.getComplemento());
		this.getPessoa().setEndereco(this.getEndereco());
		this.getPessoa().setEmail(this.getEmailPrincipal());
		this.getPessoa().setSetor(this.getSetor());
		this.getPessoa().setTelefoneComer(this.getTelefoneComercial());
		this.getPessoa().setCidade(this.getCidade());
		this.getPessoa().setCelular(this.getCelular());
		this.getPessoa().setRG(this.getRg());
		this.getPessoa().setTelefoneRecado(this.getTelefoneRecado());
		this.getPessoa().setTelefoneRes(this.getTelefoneResidencial());
		this.getPessoa().setArquivoImagem(this.getArquivoFoto());
		this.getPessoa().setSexo(this.getSexo());
		this.getPessoa().setDataNasc(this.getDataNascimento());

		this.getPessoa().setCargoPessoaEmpresa(this.getCargo());
		this.getPessoa().setDataEmissaoRG(this.getDataExpedicao());
		this.getPessoa().setEmail2(this.getEmailSecundario());
		this.getPessoa().setEstadoCivil(this.getEstadoCivil());
		this.getPessoa().setEstadoEmissaoRG(this.getEstadoEmissor());
		this.getPessoa().setNumero(this.getNumero());
		this.getPessoa().setOrgaoEmissor(this.getOrgaoEmissor());
		this.getPessoa().setNacionalidade(this.getNacionalidade());
		this.getPessoa().setNaturalidade(this.getNaturalidade());
		this.getPessoa().setNomeEmpresa(this.getNomeEmpresa());
		this.getPessoa().setCargoPessoaEmpresa(this.getCargo());

		if (!this.getNomePai().trim().isEmpty()) {
			FiliacaoVO filiacaoPai = new FiliacaoVO();
			PessoaVO pessoaPai = new PessoaVO();
			filiacaoPai.setTipo("PA");
			pessoaPai.setNome(this.getNomePai());
			pessoaPai.setGerarNumeroCPF(true);
			filiacaoPai.setPais(pessoaPai);
			try {
				this.getPessoa().adicionarObjFiliacaoVOs(filiacaoPai);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!this.getNomeMae().trim().isEmpty()) {
			FiliacaoVO filiacaoMae = new FiliacaoVO();
			PessoaVO pessoaMae = new PessoaVO();
			filiacaoMae.setTipo("MA");
			pessoaMae.setNome(this.getNomeMae());
			pessoaMae.setGerarNumeroCPF(true);
			filiacaoMae.setPais(pessoaMae);
			try {
				this.getPessoa().adicionarObjFiliacaoVOs(filiacaoMae);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (FormacaoAcademicaVO formacaoAcademicaVO : this.getFormacaoAcademicaVOs()) {
			this.getPessoa().getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
		}
	}

	/*public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}*/

	public String getNomeEmpresa() {
		if (nomeEmpresa == null) {
			nomeEmpresa = "";
		}
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public String getTelefoneEmpresa() {
		if (telefoneEmpresa == null) {
			telefoneEmpresa = "";
		}
		return telefoneEmpresa;
	}

	public void setTelefoneEmpresa(String telefoneEmpresa) {
		this.telefoneEmpresa = telefoneEmpresa;
	}

	public String getCargo() {
		if (cargo == null) {
			cargo = "";
		}
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getDataCadastro_Apresentar() {
		return (Uteis.getData(getDataCadastro()));
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public HistoricoFollowUpVO getHistoricoFollowUp() {
		if (historicoFollowUp == null) {
			historicoFollowUp = new HistoricoFollowUpVO();
		}
		return historicoFollowUp;
	}

	public void setHistoricoFollowUp(HistoricoFollowUpVO historicoFollowUp) {
		this.historicoFollowUp = historicoFollowUp;
	}

	public FuncionarioVO getConsultorPadrao() {
		if (consultorPadrao == null) {
			consultorPadrao = new FuncionarioVO();
		}
		return consultorPadrao;
	}

	public void setConsultorPadrao(FuncionarioVO consultorPadrao) {
		this.consultorPadrao = consultorPadrao;
	}

	public String getOrgaoEmissor() {
		if (orgaoEmissor == null) {
			orgaoEmissor = "";
		}
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getEstadoEmissor() {
		if (estadoEmissor == null) {
			estadoEmissor = "";
		}
		return estadoEmissor;
	}

	public void setEstadoEmissor(String estadoEmissor) {
		this.estadoEmissor = estadoEmissor;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEstadoCivil() {
		if (estadoCivil == null) {
			estadoCivil = "";
		}
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public PaizVO getNacionalidade() {
		if (nacionalidade == null) {
			nacionalidade = new PaizVO();
		}
		return nacionalidade;
	}

	public void setNacionalidade(PaizVO nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public CidadeVO getNaturalidade() {
		if (naturalidade == null) {
			naturalidade = new CidadeVO();
		}
		return naturalidade;
	}

	public void setNaturalidade(CidadeVO naturalidade) {
		this.naturalidade = naturalidade;
	}

	public Date getDataExpedicao() {
		return dataExpedicao;
	}

	public String getDataExpedicao_Apresentar() {
		return Uteis.getData(dataExpedicao);
	}

	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	public String getNomePai() {
		if (nomePai == null) {
			nomePai = "";
		}
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		if (nomeMae == null) {
			nomeMae = "";
		}
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public Boolean getParticipaBancoCurriculum() {
		if (participaBancoCurriculum == null) {
			participaBancoCurriculum = Boolean.TRUE;
		}
		return participaBancoCurriculum;
	}

	public void setParticipaBancoCurriculum(Boolean participaBancoCurriculum) {
		this.participaBancoCurriculum = participaBancoCurriculum;
	}

	public Boolean getDivulgarMeusDados() {
		if (divulgarMeusDados == null) {
			divulgarMeusDados = Boolean.TRUE;
		}
		return divulgarMeusDados;
	}

	public void setDivulgarMeusDados(Boolean divulgarMeusDados) {
		this.divulgarMeusDados = divulgarMeusDados;
	}

	public List<FormacaoAcademicaVO> getFormacaoAcademicaVOs() {
		if (formacaoAcademicaVOs == null) {
			formacaoAcademicaVOs = new ArrayList<FormacaoAcademicaVO>(0);
		}
		return formacaoAcademicaVOs;
	}

	public void setFormacaoAcademicaVOs(List<FormacaoAcademicaVO> formacaoAcademicaVOs) {
		this.formacaoAcademicaVOs = formacaoAcademicaVOs;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public UsuarioVO getUsuarioAlteracao() {
		if (usuarioAlteracao == null) {
			usuarioAlteracao = new UsuarioVO();
		}
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioVO usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	/**
	 * @return the preInscricao
	 */
	public PreInscricaoVO getPreInscricao() {
		if (preInscricao == null) {
			preInscricao = new PreInscricaoVO();
		}
		return preInscricao;
	}

	/**
	 * @param preInscricao
	 *            the preInscricao to set
	 */
	public void setPreInscricao(PreInscricaoVO preInscricao) {
		this.preInscricao = preInscricao;
	}

	/**
	 * @return the tipoOrigemCadastro
	 */
	public TipoOrigemCadastroProspectEnum getTipoOrigemCadastro() {
		if (tipoOrigemCadastro == null) {
			tipoOrigemCadastro = TipoOrigemCadastroProspectEnum.MANUAL;
		}
		return tipoOrigemCadastro;
	}

	/**
	 * @param tipoOrigemCadastro
	 *            the tipoOrigemCadastro to set
	 */
	public void setTipoOrigemCadastro(TipoOrigemCadastroProspectEnum tipoOrigemCadastro) {
		this.tipoOrigemCadastro = tipoOrigemCadastro;
	}

	/**
	 * @return the inativo
	 */
	public Boolean getInativo() {
		if (inativo == null) {
			inativo = Boolean.FALSE;
		}
		return inativo;
	}

	/**
	 * @param inativo
	 *            the inativo to set
	 */
	public void setInativo(Boolean inativo) {
		this.inativo = inativo;
	}

	/**
	 * @return the motivoInativacao
	 */
	public String getMotivoInativacao() {
		if (motivoInativacao == null) {
			motivoInativacao = "";
		}
		return motivoInativacao;
	}

	/**
	 * @param motivoInativacao
	 *            the motivoInativacao to set
	 */
	public void setMotivoInativacao(String motivoInativacao) {
		this.motivoInativacao = motivoInativacao;
	}

	/**
	 * @return the responsavelInativacao
	 */
	public UsuarioVO getResponsavelInativacao() {
		if (responsavelInativacao == null) {
			responsavelInativacao = new UsuarioVO();
		}
		return responsavelInativacao;
	}

	/**
	 * @param responsavelInativacao
	 *            the responsavelInativacao to set
	 */
	public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
		this.responsavelInativacao = responsavelInativacao;
	}

	/**
	 * @return the responsavelCadastro
	 */
	public UsuarioVO getResponsavelCadastro() {
		if (responsavelCadastro == null) {
			responsavelCadastro = new UsuarioVO();
		}
		return responsavelCadastro;
	}

	/**
	 * @param responsavelCadastro
	 *            the responsavelCadastro to set
	 */
	public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
		this.responsavelCadastro = responsavelCadastro;
	}

	public List<ProspectSegmentacaoOpcaoVO> getProspectSegmentacaoOpcaoVOs() {

		if (prospectSegmentacaoOpcaoVOs == null) {
			prospectSegmentacaoOpcaoVOs = new ArrayList<ProspectSegmentacaoOpcaoVO>(0);
		}

		return prospectSegmentacaoOpcaoVOs;
	}

	public void setProspectSegmentacaoOpcaoVOs(List<ProspectSegmentacaoOpcaoVO> prospectSegmentacaoOpcaoVOs) {
		this.prospectSegmentacaoOpcaoVOs = prospectSegmentacaoOpcaoVOs;
	}

    /**
     * @return the tipoMidia
     */
    public TipoMidiaCaptacaoVO getTipoMidia() {
        if (tipoMidia == null) {
            tipoMidia = new TipoMidiaCaptacaoVO();
        }
        return tipoMidia;
    }

    /**
     * @param tipoMidia the tipoMidia to set
     */
    public void setTipoMidia(TipoMidiaCaptacaoVO tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

	

	public Boolean getFinalizarCompromissoParalizado() {
		if(finalizarCompromissoParalizado == null){
			finalizarCompromissoParalizado = true;
		}
		return finalizarCompromissoParalizado;
	}

	public void setFinalizarCompromissoParalizado(Boolean finalizarCompromissoParalizado) {
		this.finalizarCompromissoParalizado = finalizarCompromissoParalizado;
	}

	public String getAcaoCompromissoAguardandoExcecucao() {
		if(acaoCompromissoAguardandoExcecucao == null){
			acaoCompromissoAguardandoExcecucao = "EXCLUIR";
		}
		return acaoCompromissoAguardandoExcecucao;
	}

	public void setAcaoCompromissoAguardandoExcecucao(String acaoCompromissoAguardandoExcecucao) {
		this.acaoCompromissoAguardandoExcecucao = acaoCompromissoAguardandoExcecucao;
	}

	public Boolean getConsultorAlterado() {
		if(consultorAlterado == null){
			consultorAlterado = false;
		}
		return consultorAlterado;
	}

	public void setConsultorAlterado(Boolean consultorAlterado) {
		this.consultorAlterado = consultorAlterado;
	}

	public Boolean getExcluirCompromissoNaoIniciadoPassado() {
		if(excluirCompromissoNaoIniciadoPassado == null){
			excluirCompromissoNaoIniciadoPassado = true;
		}
		return excluirCompromissoNaoIniciadoPassado;
	}

	public void setExcluirCompromissoNaoIniciadoPassado(Boolean excluirCompromissoNaoIniciadoPassado) {
		this.excluirCompromissoNaoIniciadoPassado = excluirCompromissoNaoIniciadoPassado;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 18/05/2015
	 */
	private Boolean selecionado;


	public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public FuncionarioVO getConsultorUltimaInteracao() {
		if (consultorUltimaInteracao == null) {
			consultorUltimaInteracao = new FuncionarioVO();
		}
		return consultorUltimaInteracao;
	}

	public void setConsultorUltimaInteracao(FuncionarioVO consultorUltimaInteracao) {
		this.consultorUltimaInteracao = consultorUltimaInteracao;
	}

	public FuncionarioVO getConsultorSugerido() {
		if (consultorSugerido == null) {
			consultorSugerido = new FuncionarioVO();
		}
		return consultorSugerido;
	}

	public void setConsultorSugerido(FuncionarioVO consultorSugerido) {
		this.consultorSugerido = consultorSugerido;
	}
	
	public Boolean getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = false;
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(Boolean responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public Boolean getSincronizadoRDStation() {
		if( sincronizadoRDStation == null) {
			sincronizadoRDStation = false;
		}
		return sincronizadoRDStation;
	}

	public void setSincronizadoRDStation(Boolean sincronizadoRDStation) {
		this.sincronizadoRDStation = sincronizadoRDStation;
	}

	public String getLogSincronizacaoRD() {
		if( logSincronizacaoRD == null) {
			logSincronizacaoRD = "";
		}
		return logSincronizacaoRD;
	}

	public void setLogSincronizacaoRD(String logSincronizacaoRD) {
		this.logSincronizacaoRD = logSincronizacaoRD;
	}
	
	public String getNomeBatismo() {
		if(nomeBatismo == null) {
			nomeBatismo = "";
		}else {
			if (nomeBatismo.contains("''")) {
				nomeBatismo = nomeBatismo.replaceAll("''", "'");
	        } 
		}
		return nomeBatismo;
	}

	public void setNomeBatismo(String nomeBatismo) {
		this.nomeBatismo = nomeBatismo;
	}	
}