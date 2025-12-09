package controle.arquitetura;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.MapaTurmasOfertadasVO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.secretaria.LancamentoNotaControle;
import jobs.JobProcessaRequerimentoTransferenciaNovaMatricula;
import jobs.JobRemoverPreMatriculaAluno;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.FavoritoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.TipoPerfilAcessoPermissaoEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.OpcaoPerfilAcesso;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

@Scope("singleton")
@Controller("CorrecaoBancoDadosControle")
public class CorrecaoBancoDadosControle extends SuperControle {
	
	public List<SelectItem> listaSelectItemJobNotificacao;
	public Integer loteFile;
	public List<File> fileVOs;
	public List<ArquivoVO> arquivoVOs; 
	private JobsEnum job;
	
	private String mat;
	private Integer convenio;
	private Integer parceiro;
	// Campo utilizado para processar os nosso numeros separados por virgula e entre parenteses.
	private String textoNossoNumero;
	private Boolean considerarDataFutura;
	private Boolean considerarPeriodoVencimento;
	private Date dataInicioPeriodoVencimento;
	private Date dataFimPeriodoVencimento;
	private String textoRequerimento;
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1553405380184059091L;
        private Date dataInicioAtualizar;
        
	
	public void realizarCorrecaoValorRecebidoPlanoDescontoEConvenioContaReceber(){
		try{
			getFacadeFactory().getContaReceberFacade().realizarCorrecaoValorRecebidoPlanoDescontoEConvenioContaReceber(new UsuarioVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarProcessamentoCalculoValorPrimeiraFaixaDesconto(){
		try{
			getFacadeFactory().getContaReceberFacade().realizarProcessamentoCalculoValorPrimeiraFaixaDesconto(getConfiguracaoFinanceiroPadraoSistema(), new UsuarioVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarProcessamentoCalculoValorTemporarioAReceber(){
		try{
			if (getConsiderarPeriodoVencimento() && (!Uteis.isAtributoPreenchido(getDataInicioPeriodoVencimento()) || !Uteis.isAtributoPreenchido(getDataFimPeriodoVencimento()))) {
				throw new Exception("Necessario Informar Data Inicio e Fim do Periodo Vencimento");
			}
			getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, null, true, getCompentenciaExecutarContaAReceber(), getConsiderarDataFutura(), getUsuarioLogado(), false , getConsiderarPeriodoVencimento() , getDataInicioPeriodoVencimento() , getDataFimPeriodoVencimento(), false);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarCorrecaoDistribuicaoDescontoQuandoValorDescontoForMaiorQueValorDaConta(){
		try{
			getFacadeFactory().getContaReceberFacade().executarCorrecaoBaseDadosDistribuicaoDescontosQuandoDescontoForMaiorQueValorContaReceber(this.getDataInicioAtualizar(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void corrigeHorarioProfessorComBaseTurma(){
		try{
			getFacadeFactory().getHorarioProfessorFacade().corrigeHorarioProfessorComBaseTurma();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void atualizarCodigoBarraRegistroDetalhe() {
		
	}
	
	public void corrigirDataConclusaoCursoUniRV() {
		try {
			getFacadeFactory().getMatriculaFacade().executarCorrecaoDataConclusaoCursoUniRV(getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

    /**
     * @return the dataInicioAtualizar
     */
    public Date getDataInicioAtualizar() {
        if (dataInicioAtualizar == null) {
            try {
                dataInicioAtualizar = Uteis.getDate("01/01/2013");
            } catch (Exception e) {
                dataInicioAtualizar = new Date();
            }
        }
        return dataInicioAtualizar;
    }

    /**
     * @param dataInicioAtualizar the dataInicioAtualizar to set
     */
    public void setDataInicioAtualizar(Date dataInicioAtualizar) {
        this.dataInicioAtualizar = dataInicioAtualizar;
    }
    
    public void executarCriacaoHorarioTurno(){
    	try {
			getFacadeFactory().getTurnoFacade().realizarCriacaoHorarioTurnoFixo();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }
    
	public void executarJobRemoverPreMatriculaAluno() {
		try {
			new JobRemoverPreMatriculaAluno().run();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private Integer unidadeEnsino;
	private Integer curso;
	private Integer turno;
	private Integer turma;
	private Integer disciplina;
	private String ano;
	private String semestre;
	private String nivelEducacional;
	private List<SelectItem> listaSelectItemNivelEducacional;
	private Integer configuracaoAcademica;
	private String situacoesMatriculaPeriodo;


	/**
	 * @return the unidadeEnsino
	 */
	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = 0;
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	/**
	 * @return the curso
	 */
	public Integer getCurso() {
		if (curso == null) {
			curso = 0;
		}
		return curso;
	}

	/**
	 * @param curso the curso to set
	 */
	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	/**
	 * @return the turno
	 */
	public Integer getTurno() {
		if (turno == null) {
			turno = 0;
		}
		return turno;
	}

	/**
	 * @param turno the turno to set
	 */
	public void setTurno(Integer turno) {
		this.turno = turno;
	}

	/**
	 * @return the turma
	 */
	public Integer getTurma() {
		if (turma == null) {
			turma = 0;
		}
		return turma;
	}

	/**
	 * @param turma the turma to set
	 */
	public void setTurma(Integer turma) {
		this.turma = turma;
	}

	/**
	 * @return the disciplina
	 */
	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	/**
	 * @param disciplina the disciplina to set
	 */
	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	/**
	 * @param ano the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	/**
	 * @param semestre the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the configuracaoAcademica
	 */
	public Integer getConfiguracaoAcademica() {
		if (configuracaoAcademica == null) {
			configuracaoAcademica = 0;
		}
		return configuracaoAcademica;
	}

	/**
	 * @param configuracaoAcademica the configuracaoAcademica to set
	 */
	public void setConfiguracaoAcademica(Integer configuracaoAcademica) {
		this.configuracaoAcademica = configuracaoAcademica;
	}

	/**
	 * @return the situacoesMatriculaPeriodo
	 */
	public String getSituacoesMatriculaPeriodo() {
		if (situacoesMatriculaPeriodo == null) {
			situacoesMatriculaPeriodo = "'AT', 'FI', 'PR'";
		}
		return situacoesMatriculaPeriodo;
	}

	/**
	 * @param situacoesMatriculaPeriodo the situacoesMatriculaPeriodo to set
	 */
	public void setSituacoesMatriculaPeriodo(String situacoesMatriculaPeriodo) {
		this.situacoesMatriculaPeriodo = situacoesMatriculaPeriodo;
	}
	
	public void realizarCalculoMediaFinal(){
		try{
			setLogEad("");
			LancamentoNotaControle lancamentoNotaControle = (LancamentoNotaControle)getControlador("LancamentoNotaControle");				
			StringBuilder sql = lancamentoNotaControle.realizarCalculoMediaFinal(getUnidadeEnsino(), getCurso(), getTurno(), getTurma(), getDisciplina(), getAno(), getSemestre(), getConfiguracaoAcademica(), getSituacoesMatriculaPeriodo().trim().isEmpty()?null:getSituacoesMatriculaPeriodo(), getNivelEducacional());			
			setMensagemDetalhada("", sql.toString());
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the nivelEducacional
	 */
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	/**
	 * @param nivelEducacional the nivelEducacional to set
	 */
	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	/**
	 * @return the listaSelectItemNivelEducacional
	 */
	public List<SelectItem> getListaSelectItemNivelEducacional() {
		if (listaSelectItemNivelEducacional == null) {
			listaSelectItemNivelEducacional = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		}
		return listaSelectItemNivelEducacional;
	}

	/**
	 * @param listaSelectItemNivelEducacional the listaSelectItemNivelEducacional to set
	 */
	public void setListaSelectItemNivelEducacional(List<SelectItem> listaSelectItemNivelEducacional) {
		this.listaSelectItemNivelEducacional = listaSelectItemNivelEducacional;
	}

	/**
	 * @return the listaSelectItemJobNotificacao
	 */
	public List<SelectItem> getListaSelectItemJobNotificacao() {
		if (listaSelectItemJobNotificacao == null) {
			listaSelectItemJobNotificacao = new ArrayList<SelectItem>(0);
			for(JobsEnum job:JobsEnum.values()){
				listaSelectItemJobNotificacao.add(new SelectItem(job, job.getName()));
			}
		}
		return listaSelectItemJobNotificacao;
	}

	/**
	 * @param listaSelectItemJobNotificacao the listaSelectItemJobNotificacao to set
	 */
	public void setListaSelectItemJobNotificacao(List<SelectItem> listaSelectItemJobNotificacao) {
		this.listaSelectItemJobNotificacao = listaSelectItemJobNotificacao;
	}

	/**
	 * @return the job
	 */
	public JobsEnum getJob() {
		if (job == null) {
			job = JobsEnum.values()[0];
		}
		return job;
	}

	/**
	 * @param job the job to set
	 */
	public void setJob(JobsEnum job) {
		this.job = job;
	}
	
	public void executarJob(){
		FacesContext contexto = FacesContext.getCurrentInstance();
		Object classe = null;
		Method metodo = null;
		try {						
			classe = contexto.getELContext().getELResolver().getValue(contexto.getELContext(), null, getJob().getClasse());
			metodo = classe.getClass().getMethod(getJob().getMetodo());
			metodo.invoke(classe);
		} catch (Exception e) {		
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			classe = null;
			metodo = null;
		}
	}
	
	private StringBuilder permissao = new StringBuilder("");
	private StringBuilder subModulos = new StringBuilder("");
	private StringBuilder properties = new StringBuilder("");
	private StringBuilder propertieSubModulos = new StringBuilder("");

	public void gerarPermissao(){
		try {
			permissao = new StringBuilder("");
			subModulos = new StringBuilder("");
			Map<String, String> mapFav = new HashMap<String, String>(0);
			List<FavoritoVO> favoritoVOs = getFacadeFactory().getFavoritoFacade().consultarPorUsuario(0, TipoVisaoEnum.ADMINISTRATIVA, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			q:
			for (FavoritoVO favoritoVO : favoritoVOs) {
				if(favoritoVO.getPagina().equals("relatorio/acesoCatracaRel.jsp")){
					favoritoVO.setPagina("relatorio/acessoCatracaRel.jsp");
				}
				if(favoritoVO.getPagina().equals("relatorio/reservaCatalogoRelRel.jsp")){
					favoritoVO.setPagina("relatorio/reservaCatalogoRel.jsp");
				}
				if(mapFav.containsKey(favoritoVO.getPagina())){
					 if(mapFav.get(favoritoVO.getPagina()).trim().isEmpty()){
						 getFacadeFactory().getFavoritoFacade().excluir(favoritoVO);
					 }else{						 
						 favoritoVO.setPagina(mapFav.get(favoritoVO.getPagina()));						 
						 getFacadeFactory().getFavoritoFacade().excluir(favoritoVO);
						 favoritoVO.setCodigo(0);
						 getFacadeFactory().getFavoritoFacade().incluir(favoritoVO, false, getUsuarioLogado());
					 }
					 
				}else{
					
					if(favoritoVO.getPagina().contains(".jsp")){
						if(favoritoVO.getPagina().equals("ConverteGetParaPostServletInteracao?pagina=interacaoWorkflowForm.jsp&param=codigo&valor=0&paramEtapaAtual=etapaAtual&valorEtapaAtual=0&paramNovoProspect=novoProspect&valorNovoProspect=true")){
							mapFav.put(favoritoVO.getPagina(), "../../ConverteGetParaPostServletInteracao?pagina=visaoAdministrativo/crm/interacaoWorkflowForm.xhtml&amp;param=codigo&amp;valor=0&amp;paramEtapaAtual=etapaAtual&amp;valorEtapaAtual=0&amp;paramNovoProspect=novoProspect&amp;valorNovoProspect=true");
							permissao.append(favoritoVO.getPagina()).append(" = ").append("../../ConverteGetParaPostServletInteracao?pagina=visaoAdministrativo/crm/interacaoWorkflowForm.xhtml&amp;param=codigo&amp;valor=0&amp;paramEtapaAtual=etapaAtual&amp;valorEtapaAtual=0&amp;paramNovoProspect=novoProspect&amp;valorNovoProspect=true");
							permissao.append("\n");
							favoritoVO.setPagina("../../ConverteGetParaPostServletInteracao?pagina=visaoAdministrativo/crm/interacaoWorkflowForm.xhtml&amp;param=codigo&amp;valor=0&amp;paramEtapaAtual=etapaAtual&amp;valorEtapaAtual=0&amp;paramNovoProspect=novoProspect&amp;valorNovoProspect=true");
							getFacadeFactory().getFavoritoFacade().excluir(favoritoVO);
							favoritoVO.setCodigo(0);
							getFacadeFactory().getFavoritoFacade().incluir(favoritoVO, false, getUsuarioLogado());
							continue q;							
						}
						
					String telaComp = favoritoVO.getPagina().replace(".jsp", "").replace("2", "").replace("faces/", "");
					boolean relatorio = telaComp.contains("relatorio/");
					if(relatorio){
						telaComp = telaComp.substring(telaComp.lastIndexOf("/")+1, telaComp.length());
					}					
					String param = "";
					if(favoritoVO.getPagina().contains("?")){
						param = favoritoVO.getPagina().substring(favoritoVO.getPagina().lastIndexOf("?"), favoritoVO.getPagina().length());
					    telaComp = telaComp.substring(0, telaComp.lastIndexOf("?"));
					}
					if(telaComp.equals("mapaPendenciasControleCobranca")){
						telaComp = "mapaPendenciasControleCobrancaForm";
					}
					for (PerfilAcessoModuloEnum perfilAcessoModuloEnum : PerfilAcessoModuloEnum.values()) {
						if (perfilAcessoModuloEnum.getPerfilAcessoPermissaoEnumInterfaces() != null 
								&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_ALUNO) 
								&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_COORDENADOR) 
								&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_PARCEIRO) 
								&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_PROFESSOR)) {
							for (Enum<? extends PerfilAcessoPermissaoEnumInterface> perfil : perfilAcessoModuloEnum
									.getPerfilAcessoPermissaoEnumInterfaces()) {
								PerfilAcessoPermissaoEnumInterface perfilAcessoPermissaoEnumInterface = ((PerfilAcessoPermissaoEnumInterface) perfil);
								if(!perfilAcessoPermissaoEnumInterface.getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE)
										|| perfilAcessoPermissaoEnumInterface.getValor().equalsIgnoreCase("mapaRegistroAbandonoCursoTrancamento")){
								List<String> telas = perfilAcessoPermissaoEnumInterface.getPaginaAcessoVisao(TipoVisaoEnum.ADMINISTRATIVA);
								if(telas != null){
								for (String telaNova : telas) {									
									String telaComp2 = telaNova.replace(".xhtml", "").replace("2", "");									
									if(telaComp2.equalsIgnoreCase(telaComp)){
										String pacote = "";
										if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.ACADEMICO)){
											pacote = "academico";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.ADMINISTRATIVO)){
											pacote = "administrativo";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.AVALIACAO_INSTITUCIONAL)){
											pacote = "avaliacaoInstitucional";
//										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.BANCO_DE_CURRICULOS)){
//											pacote = "bancoCurriculos";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.BIBLIOTECA)){
											pacote = "biblioteca";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.COMPRAS)){
											pacote = "compras";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.CRM)){
											pacote = "crm";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.EAD)){
											pacote = "ead";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.FINANCEIRO) || telaComp.equals("relatorioSEIDecidirRel")){
											pacote = "financeiro";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.PLANO_ORCAMENTARIO)){
											pacote = "planoOrcamentario";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.PROCESSO_SELETIVO)){
											pacote = "processoSeletivo";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.NOTA_FISCAL)){
											pacote = "notaFiscal";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.ESTAGIO)){
											pacote = "estagio";
										}else if(perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.PATRIMONIO)){
											pacote = "patrimonio";
										}
										if(telaComp.equals("questaoCons")){
											if(param.contains("us=ex")){
												param +="&amp;idControlador=QuestaoControleTUex";
											}else if(param.contains("us=on")){
												param +="&amp;idControlador=QuestaoControleTUon";
											}else if(param.contains("us=pr")){
												param +="&amp;idControlador=QuestaoControleTUpr";
											}
										}
										if(telaComp.equals("questionarioCons") || telaComp.equals("perguntaCons")){
											if(telaComp.equals("questionarioCons")){
												if(param.contains("escopo=RE")){
													param +="&amp;idControlador=QuestionarioControleESCOPORE";
												}else{
													param +="&amp;idControlador=QuestionarioControleESCOPOPS";
												}												
											}else{
												if(param.contains("escopo=RE")){
													param +="&amp;idControlador=QuestaoControleESCOPORE";
												}else{
													param +="&amp;idControlador=QuestaoControleESCOPOPS";
												}
											}
											pacote = "academico";
										}
										if(telaComp.equals("relatorioSEIDecidirRel")){
											pacote = "financeiro";
										}
										if(relatorio ||  perfilAcessoPermissaoEnumInterface.getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.RELATORIO)){
											pacote += "/relatorio";
										}										
										mapFav.put(favoritoVO.getPagina(), "../"+pacote+"/"+telaNova+param);
										permissao.append(favoritoVO.getPagina()).append(" = ").append("../"+pacote+"/"+telaNova+param);
										permissao.append("\n");
										favoritoVO.setPagina("../"+pacote+"/"+telaNova+param);
										favoritoVO.setPropertMenu(perfilAcessoPermissaoEnumInterface.getDescricaoVisao(TipoVisaoEnum.ADMINISTRATIVA));
										getFacadeFactory().getFavoritoFacade().excluir(favoritoVO);
										favoritoVO.setCodigo(0);
										getFacadeFactory().getFavoritoFacade().incluir(favoritoVO, false, getUsuarioLogado());
										continue q;
									}
								}
								}
								}
							}
						}
						}
					getFacadeFactory().getFavoritoFacade().excluir(favoritoVO);
					subModulos.append(favoritoVO.getPagina());
					subModulos.append("\n");
					mapFav.put(favoritoVO.getPagina(), "");
					}
				}
			}
		} catch (Exception e) {
			subModulos.append(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StringBuilder criarPermissao(String modulo, String subModulo, String keySubModulo, String per, String perSup, OpcaoPerfilAcesso op){
		int tam= per.length();
		StringBuilder key = new StringBuilder("");
		StringBuilder enumPer = new StringBuilder("");
		StringBuilder propTit = new StringBuilder("");
		StringBuilder propAju = new StringBuilder("");
		for(int x=0; x < tam;x++){			
			if(key.length() != 0 && Character.isUpperCase(per.charAt(x)) && !Character.isUpperCase(per.charAt(x-1)) && !String.valueOf(per.charAt(x-1)).equals("_")){
				key.append("_").append(String.valueOf(per.charAt(x)).toUpperCase());
			}else{
				key.append(String.valueOf(per.charAt(x)).toUpperCase());
			}
		}
		propTit.append("per_").append(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(per)).replaceAll(" ", ""));
		propAju.append(propTit).append("_ajuda");
		propTit.append("_titulo");
		String tela = op.getUrl() != null ? op.getUrl().replace("abrirPopup(\'", ""): "";
		if(!tela.isEmpty() && tela.contains("\'")){
			tela = tela.substring(0, tela.indexOf("\'"));
			tela = tela.replaceAll(".jsp", ".xhtml").replaceAll("2", "");			
			if(tela.contains("/")){
				tela = tela.substring(tela.indexOf("/")+1, tela.length());
			}
			if(tela.endsWith("Cons.xhtml")){
				tela = tela+"\",\""+(tela.replace("Cons.xhtml", "Form.xhtml"));
			}
		}
		enumPer.append(key).append("(\"").append(per).append("\", new Permissao[] {\n");					
		if(modulo.equals("Visão do Professor")){						
			enumPer.append(" new PermissaoVisao( ");
			enumPer.append("\n");
			enumPer.append(" TipoVisaoEnum.PROFESSOR, ");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propTit).append("\"),");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propAju).append("\")");
			if(tela.isEmpty()){
				enumPer.append(")");
			}else{
				enumPer.append(", new String[]{\"").append(tela).append("\"})");
			}
		}else if(modulo.equals("Visão do Coordenador")){
			enumPer.append(" new PermissaoVisao( ");
			enumPer.append("\n");
			enumPer.append(" TipoVisaoEnum.COORDENADOR, ");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propTit).append("\"),");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propAju).append("\")");
			if(tela.isEmpty()){
				enumPer.append(")");
			}else{
				enumPer.append(", new String[]{\"").append(tela).append("\")");
			}
		}else if(modulo.equals("Visão do Aluno")){
			enumPer.append(" new PermissaoVisao( ");
			enumPer.append("\n");
			enumPer.append(" TipoVisaoEnum.ALUNO, ");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propTit).append("\"),");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propAju).append("\")");
			if(tela.isEmpty()){
				enumPer.append("), ");
			}else{
				enumPer.append(", new String[]{\"").append(tela).append("\"}), ");
			}
			enumPer.append(" new PermissaoVisao( ");
			enumPer.append("\n");
			enumPer.append(" TipoVisaoEnum.PAIS, ");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propTit).append("\"),");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propAju).append("\")");
			if(tela.isEmpty()){
				enumPer.append(")");
			}else{
				enumPer.append(", new String[]{\"").append(tela).append("\"})");
			}
		}else if(modulo.equals("Visão do Parceiro")){
			enumPer.append(" new PermissaoVisao( ");
			enumPer.append("\n");
			enumPer.append(" TipoVisaoEnum.PARCEIRO, ");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propTit).append("\"),");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propAju).append("\")");
			if(tela.isEmpty()){
				enumPer.append(")");
			}else{
				enumPer.append(", new String[]{\"").append(tela).append("\"})");
			}
		}else if(modulo.equals("Home Candidato")){
			enumPer.append(" new PermissaoVisao( ");
			enumPer.append("\n");
			enumPer.append(" TipoVisaoEnum.CANDIDATO, ");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propTit).append("\"),");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propAju).append("\")");
			if(tela.isEmpty()){
				enumPer.append(")");
			}else{
				enumPer.append(", new String[]{\"").append(tela).append("\"})");
			}
		}else{
			enumPer.append(" new PermissaoVisao( ");
			enumPer.append("\n");
			enumPer.append(" TipoVisaoEnum.ADMINISTRATIVA, ");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propTit).append("\"),");
			enumPer.append("UteisJSF.internacionalizar(\"").append(propAju).append("\")");
			if(tela.isEmpty()){
				enumPer.append(")");
			}else{
				enumPer.append(", new String[]{\"").append(tela).append("\"})");
			}
		}
		enumPer.append("\n},\n");
		if(op.getTipo() == OpcaoPerfilAcesso.TP_FUNCIONALIDADE){
			enumPer.append("TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE, ");
		}else if(key.toString().startsWith("RELATORIO_") || key.toString().startsWith("REL_")){
			enumPer.append("TipoPerfilAcessoPermissaoEnum.RELATORIO, ");
		}else{
			enumPer.append("TipoPerfilAcessoPermissaoEnum.ENTIDADE, ");
		}
		enumPer.append("\n");
		if(perSup.trim().isEmpty()){
			enumPer.append("null, ");
		}else{
			enumPer.append("PerfilAcessoPermissaoEnum.").append(perSup).append(", ");
		}
		enumPer.append("\n");
		enumPer.append("PerfilAcessoSubModuloEnum.").append(keySubModulo);
		enumPer.append("),");
		enumPer.append("\n");
		propTit.append("=").append(Uteis.substituirVogaisAcentuadasPoUnicode(op.getTitulo()));
		properties.append(propTit).append("\n");
		properties.append(propAju).append("=\n");		
		if(op.getAcoes() != null && !op.getAcoes().isEmpty()){
			for(String keyFun:((Map<String, OpcaoPerfilAcesso>)op.getAcoes()).keySet()){
				OpcaoPerfilAcesso opFun = (OpcaoPerfilAcesso)op.getAcoes().get(keyFun);
				enumPer.append(criarPermissao(modulo, subModulo, keySubModulo, opFun.getNome(), key.toString(), opFun)).append("\n");
			}
		}
		key = null;
		propTit = null;
		propAju = null;
		return enumPer;
	}

	/**
	 * @return the permissao
	 */
	public String getPermissao() {
		if (permissao == null) {
			permissao = new StringBuilder("");
		}
		return permissao.toString();
	}

	/**
	 * @param permissao the permissao to set
	 */
	public void setPermissao(String permissao) {
		this.permissao = new StringBuilder(permissao);
	}

	/**
	 * @return the subModulos
	 */
	public String getSubModulos() {
		if (subModulos == null) {
			subModulos = new StringBuilder("");
		}
		return subModulos.toString();
	}

	/**
	 * @param subModulos the subModulos to set
	 */
	public void setSubModulos(String subModulos) {
		this.subModulos = new StringBuilder(subModulos);
	}

	/**
	 * @return the properties
	 */
	public String getProperties() {
		if (properties == null) {
			properties = new StringBuilder("");
		}
		return properties.toString();
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(String properties) {
		this.properties = new StringBuilder(properties);
	}

	/**
	 * @return the propertieSubModulos
	 */
	public String getPropertieSubModulos() {
		if (propertieSubModulos == null) {
			propertieSubModulos = new StringBuilder("");
		}
		return propertieSubModulos.toString();
	}

	/**
	 * @param propertieSubModulos the propertieSubModulos to set
	 */
	public void setPropertieSubModulos(String propertieSubModulos) {
		this.propertieSubModulos = new StringBuilder(propertieSubModulos);
	}
	
private String tagRelatorio;
	
	
	
	public String getTagRelatorio() {
		return tagRelatorio;
	}

	public void setTagRelatorio(String tagRelatorio) {
		this.tagRelatorio = tagRelatorio;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void realizarGeracaoTagRelatorio(){		
		setTagRelatorio("");
		StringBuilder tags =  new StringBuilder();
		
		Hashtable hast = new Hashtable<>();
		hast.putAll(Dominios.getMarcadoAluno());
		hast.putAll(Dominios.getMarcadoContaReceber());
		hast.putAll(Dominios.getMarcadoCurso());
		hast.putAll(Dominios.getMarcadoDisciplina());
		hast.putAll(Dominios.getMarcadoDisciplinaDeclaracao());
		hast.putAll(Dominios.getMarcadoEstagio());
		hast.putAll(Dominios.getMarcadoInscProcSeletivo());
		hast.putAll(Dominios.getMarcadoMatricula());
		hast.putAll(Dominios.getMarcadoOutras());
		hast.putAll(Dominios.getMarcadoProfessor());
		hast.putAll(Dominios.getMarcadoUnidadeEnsino());
		
		List<String> lista = new ArrayList<String>();
		for (Object o : hast.entrySet()) {		    
			Map.Entry pair = (Map.Entry) o;
			lista.add((String)pair.getValue());			
		}
		Collections.sort(lista);
		for (String valor : lista) {
			if(valor.equals("ListaDisciplinasCursadasOuMinistradas_Disciplina") 
					|| valor.equals("ListaDemostrativoDebito_Aluno")
					|| valor.equals("ListaDisciplinasMatriculadasCurso")
					|| valor.equals("ListaDisciplinasAprovadasCertificado_Disciplina")
					|| valor.equals("ListaDisciplinasAprovadasPeriodoLetivo_Disciplina")
					|| valor.equals("ListaDisciplinasCursadasOuMinistradas_Disciplina")
					|| valor.equals("ListaDisciplinasHistoricoCertificado_Disciplina")
					|| valor.equals("ListaDisciplinasHistoricoDiploma_Disciplina")
					|| valor.equals("ListaDisciplinasHistoricoPeriodoLetivo_Disciplina")
					|| valor.equals("ListaDemostrativoDebitoNegociado_Aluno")
					|| valor.equals("TabelaAcompanhamentoFinanceiro_Matricula")
					|| valor.equals("TabelaTipoDesconto_Curso")
					|| valor.equals("TabelaTodosTipoDesconto_Curso")
					|| valor.equals("TabelaListaContaReceberComDescontos_Curso")
					|| valor.equals("ListaDisciplinasHistoricoCertificado_Disciplina")){
				tags.append("<parameter name=\"").append(valor).append("\" class=\"java.util.List\" isForPrompting=\"false\"/>").append(" \r\n");
			}else{
				tags.append("<parameter name=\"").append(valor).append("\" class=\"java.lang.String\" isForPrompting=\"false\"/>").append(" \r\n");
			}
		}
		tags.append("<parameter name=\"logoPadraoRelatorio\" class=\"java.lang.String\" isForPrompting=\"false\"/>").append(" \r\n");
		tags.append("<parameter name=\"nomeUsuario\" class=\"java.lang.String\" isForPrompting=\"false\"/>").append(" \r\n");
		tags.append("<parameter name=\"SUBREPORT_DIR\" class=\"java.lang.String\" isForPrompting=\"false\"/>").append(" \r\n");
		
		setTagRelatorio(tags.toString());
	}
	
	private String sqlLancamentoContabil;
	
	public String getSqlLancamentoContabil() {
		if (sqlLancamentoContabil == null) {
			sqlLancamentoContabil = "";
		}
		return sqlLancamentoContabil;
	}

	public void setSqlLancamentoContabil(String sqlLancamentoContabil) {
		this.sqlLancamentoContabil = sqlLancamentoContabil;
	}

	public void realizarCorrecaoLancamentoContabil() {
		try {
			
			Uteis.checkState(!Uteis.isAtributoPreenchido(getSqlLancamentoContabil()), "O texto Sql deve estar Preenchido");
			getFacadeFactory().getContaReceberFacade().realizarCorrecaoLancamentoContabilPorSql(getSqlLancamentoContabil(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(0), getUsuarioLogado());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	

	public Integer getLoteFile() {
		if(loteFile == null){
			loteFile = 0;
		}
		return loteFile;
	}

	public void setLoteFile(Integer loteFile) {
		this.loteFile = loteFile;
	}

	public List<File> getFileVOs() {
		if(fileVOs == null){
			fileVOs = new ArrayList<File>(0);
		}
		return fileVOs;
	}

	public void setFileVOs(List<File> fileVOs) {
		this.fileVOs = fileVOs;
	}

	public List<ArquivoVO> getArquivoVOs() {
		if(arquivoVOs == null){
			arquivoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoVOs;
	}

	public void setArquivoVOs(List<ArquivoVO> arquivoVOs) {
		this.arquivoVOs = arquivoVOs;
	}
	
	private boolean filtarArquivo;
	private boolean filtarImagem;
	
	
	public boolean isFiltarArquivo() {
		return filtarArquivo;
	}

	public void setFiltarArquivo(boolean filtarArquivo) {
		this.filtarArquivo = filtarArquivo;
	}

	public boolean isFiltarImagem() {
		return filtarImagem;
	}

	public void setFiltarImagem(boolean filtarImagem) {
		this.filtarImagem = filtarImagem;
	}

	public void consultarArquivosNaoVinculadosSistema(){
		try {
			setFileVOs(getFacadeFactory().getArquivoFacade().consultarArquivosNaoVinculadosSistema(getConfiguracaoGeralPadraoSistema(), isFiltarArquivo(), isFiltarImagem(), getLoteFile()));
			setTamanhoTotal(0l);
			for(File file: getFileVOs()){
				setTamanhoTotal(getTamanhoTotal()+file.length());
			}						
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private Long tamanhoTotal;	
	
	public Long getTamanhoTotal() {
		if(tamanhoTotal == null){
			tamanhoTotal = 0l;
		}
		return tamanhoTotal;
	}

	public void setTamanhoTotal(Long tamanhoTotal) {
		this.tamanhoTotal = tamanhoTotal;
	}
	
	
	public String getTamanhoTotalApresentar() {
		Double tam = Uteis.arrendondarForcando2CadasDecimais((getTamanhoTotal().doubleValue()/1024.0));
		if(tam >= 1024){
			tam = Uteis.arrendondarForcando2CadasDecimais(tam/1024);
			if(tam > 1024){
				tam = Uteis.arrendondarForcando2CadasDecimais(tam/1024);				
				return tam+" GB";
			}else{
				return tam+" MB";				
			}
		}				
		return tam+" KB";
	}

	public void deletarTodosArquivos(){
		try {
		for(File file: getFileVOs()){
			file.delete();
			setTamanhoTotal(getTamanhoTotal()-file.length());
			File direc = new File(file.getAbsolutePath().replace(file.getName(), ""));
			if(direc.isDirectory() && direc.listFiles().length == 0){
				direc.delete();
			}
		}
		setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void deletarArquivo(){
		try {
			File file = ((File)getRequestMap().get("fileItem"));
			file.delete();			
			setTamanhoTotal(getTamanhoTotal()-file.length());
			File direc = new File(file.getAbsolutePath().replace(file.getName(), ""));
			if(direc.isDirectory() && direc.listFiles().length == 0){
				direc.delete();
			}
			getFileVOs().remove(file);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}	
	
	public Map<File, Date> data;
	public Map<File, String> tamanho;
	
	public String getTamanho(){
		if(tamanho == null){
			tamanho = new HashMap<File, String>(0);
		}
		File file = ((File)getRequestMap().get("fileItem"));
		if(!tamanho.containsKey(file)){
			Double tam = Uteis.arrendondarForcando2CadasDecimais(file.length()/1024);
			if(tam >= 1024){
				tam = Uteis.arrendondarForcando2CadasDecimais(tam/1024);
				if(tam > 1024){
					tam = Uteis.arrendondarForcando2CadasDecimais(tam/1024);
					tamanho.put(file, tam+" GB");
				}else{
					tamanho.put(file, tam+" MB");
				}
			}else{				
				tamanho.put(file, tam+" KB");
			}
		}
		return tamanho.get(file);
	}
	
	public Date getDataFile(){
		if(data == null){
			data = new HashMap<File, Date>(0);
		}
		File file = ((File)getRequestMap().get("fileItem"));
		if(!data.containsKey(file)){
			data.put(file, new Date(file.lastModified()));
		}
		return data.get(file);
	}
	
	public void ziparArquivos(){
		try {
			File file = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+"BACKUP");
			if(!file.exists()){
				file.mkdirs();
			}
			file = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+"BACKUP"+File.separator+"zip_"+Uteis.getData(new Date(), "dd_MM_yyyy_HH_MM")+".zip");
			File[] files = new File[getFileVOs().size()];
			int x = 0;
			for(File fileC :getFileVOs()){
				files[x] = fileC;
				x++;
			}
			zip(files, file);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void zip(File[] files, File outputFile) throws IOException {

		if (files != null && files.length > 0) {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));			
			zipFiles(files, out);
			out.flush();
			out.close();
		}
	}

	private void zipFiles(File[] files, ZipOutputStream out) throws IOException {
		byte[] buf = new byte[1024];		
		int len;
		for (int i = 0; i < files.length; i++) {
			if (files[i] != null) {				
					FileInputStream in = new FileInputStream(files[i]);
					String path = files[i].getAbsolutePath().substring(files[i].getAbsolutePath().indexOf(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())+getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo().length(), files[i].getAbsolutePath().length());
					if(path.startsWith(File.separator)){
						path = path.substring(1, path.length());
					}
					out.putNextEntry(new ZipEntry(path));
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
						
			}
		}
	}
	
	private String logEad;
	
	public String getLogEad() {
		if(logEad == null){
			logEad = "";
		}
		return logEad;
	}

	public void setLogEad(String logEad) {
		this.logEad = logEad;
	}

	public void realizarLancamentoNotaEADHistorico(){
		try {
			setLogEad("");
			setLogEad(getFacadeFactory().getHistoricoFacade().realizarCorrecaoLancamentoNotaEADHistorico(getUnidadeEnsino(), getCurso(), getTurno(), getTurma(), getDisciplina(), getAno(), getSemestre(), getNivelEducacional(), getConfiguracaoAcademica(), getSituacoesMatriculaPeriodo(), getUsuarioLogado()).toString());
			if(getLogEad().trim().isEmpty()){
				setLogEad("Nenhum registro alterado!!!");
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void resetarAutenticacaoCliente(){
		getAplicacaoControle().setAutenticacaoRealizada(null);
		setMensagemDetalhada("msg_dados_gravados", Uteis.SUCESSO);
	}

	private List<MapaTurmasOfertadasVO> listaMapaTurmasOfertadasVO;

	public List<MapaTurmasOfertadasVO> getListaMapaTurmasOfertadasVO() {
		if(listaMapaTurmasOfertadasVO == null) {
			listaMapaTurmasOfertadasVO = new ArrayList<>();
		}
		return listaMapaTurmasOfertadasVO;
	}

	public void setListaMapaTurmasOfertadasVO(List<MapaTurmasOfertadasVO> listaMapaTurmasOfertadasVO) {
		this.listaMapaTurmasOfertadasVO = listaMapaTurmasOfertadasVO;
	}

	public void atualizarMapaTurmaOfertada(){
		try {
			getListaMapaTurmasOfertadasVO().clear();
			getAplicacaoControle().getMapTurmasOfertadas()
					.entrySet()
					.parallelStream().forEach(map->{
						MapaTurmasOfertadasVO mapa = new MapaTurmasOfertadasVO();
						mapa.setChave(map.getKey());
						mapa.setListaTurma(map.getValue());
						getListaMapaTurmasOfertadasVO().add(mapa);
					});
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private String compentenciaExecutarContaAReceber;
	private String compentenciaExecutarContaRecebida;
	
	public String getCompentenciaExecutarContaAReceber() {
		if(compentenciaExecutarContaAReceber == null){
			compentenciaExecutarContaAReceber = "";
		}
		return compentenciaExecutarContaAReceber;
	}

	public void setCompentenciaExecutarContaAReceber(String compentenciaExecutarContaAReceber) {
		this.compentenciaExecutarContaAReceber = compentenciaExecutarContaAReceber;
	}
	
	public String getCompentenciaExecutarContaRecebida() {
		if(compentenciaExecutarContaRecebida == null){
			compentenciaExecutarContaRecebida = "08/2018";
		}
		return compentenciaExecutarContaRecebida;
	}
	
	public void setCompentenciaExecutarContaRecebida(String compentenciaExecutarContaRecebida) {
		this.compentenciaExecutarContaRecebida = compentenciaExecutarContaRecebida;
	}

	public void realizaGeracaoCentroResultadoDetalheContaRecebidaNegociadaCancelada() {
		try {
			if(getCompentenciaExecutarContaRecebida().trim().isEmpty() || getCompentenciaExecutarContaRecebida().length() != 7 || !getCompentenciaExecutarContaRecebida().contains("/")) {
				throw new Exception("Formato inválido para o campo competência (MM/YYYY).");
			}
			getFacadeFactory().getContaReceberFacade().realizarGeracaoDetalhamentoValorContaRecebidaNegociacadaCancelada(null, null, getCompentenciaExecutarContaRecebida());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private ContaReceberVO contaReceberAtualizarDetalhamentoValor;
	private ContaReceberVO contaRecebidaAtualizarDetalhamentoValor;
	
	
	public ContaReceberVO getContaReceberAtualizarDetalhamentoValor() {
		if (contaReceberAtualizarDetalhamentoValor == null) {
			contaReceberAtualizarDetalhamentoValor = new ContaReceberVO();
		}
		return contaReceberAtualizarDetalhamentoValor;
	}

	public void setContaReceberAtualizarDetalhamentoValor(ContaReceberVO contaReceberAtualizarDetalhamentoValor) {
		this.contaReceberAtualizarDetalhamentoValor = contaReceberAtualizarDetalhamentoValor;
	}
	
	

	public ContaReceberVO getContaRecebidaAtualizarDetalhamentoValor() {
		if (contaRecebidaAtualizarDetalhamentoValor == null) {
			contaRecebidaAtualizarDetalhamentoValor = new ContaReceberVO();
		}
		return contaRecebidaAtualizarDetalhamentoValor;
	}

	public void setContaRecebidaAtualizarDetalhamentoValor(ContaReceberVO contaRecebidaAtualizarDetalhamentoValor) {
		this.contaRecebidaAtualizarDetalhamentoValor = contaRecebidaAtualizarDetalhamentoValor;
	}

	public void realizarProcessamentoCalculoValorTemporarioAReceber1(){
		try{
			if (getConsiderarPeriodoVencimento() && (!Uteis.isAtributoPreenchido(getDataInicioPeriodoVencimento()) || !Uteis.isAtributoPreenchido(getDataFimPeriodoVencimento()))) {
				throw new Exception("Necessario Informar Data Inicio e Fim do Periodo Vencimento");
			}
			List<ContaReceberVO> lista = new ArrayList<>();
			getFacadeFactory().getContaReceberFacade().carregarDados(getContaReceberAtualizarDetalhamentoValor(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			lista.add(getContaReceberAtualizarDetalhamentoValor());
			getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, lista, false, "", getConsiderarDataFutura(), getUsuarioLogado(), false , getConsiderarPeriodoVencimento() , getDataInicioPeriodoVencimento() , getDataFimPeriodoVencimento(), false);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarProcessamentoCalculoValorTemporarioAReceber2(){
		try{
			List<ContaReceberVO> lista = new ArrayList<>();
			getFacadeFactory().getContaReceberFacade().carregarDados(getContaRecebidaAtualizarDetalhamentoValor(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			lista.add(getContaRecebidaAtualizarDetalhamentoValor());
			getFacadeFactory().getContaReceberFacade().realizarGeracaoDetalhamentoValorContaRecebidaNegociacadaCancelada(2018, lista, "");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void processarLiberacaoSuspensaoConvenioFinanciamentoProprio(){
		try{
			getFacadeFactory().getContaReceberFacade().processarLiberacaoSuspensaoConvenioFinanciamentoProprio(getMat(), getConvenio(), getParceiro(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void processarRegerarNossoNumero(){
		try{
			if(!getTextoNossoNumero().contains("(") || !getTextoNossoNumero().contains(")")) {
				throw new Exception("Necessário colocar nosso número dentro de parenteses. Ex: ('nossonumero1', 'nossonumero2')");
			}
			getFacadeFactory().getContaReceberFacade().processarRegerarNossoNumeroContaReceber(getTextoNossoNumero(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void processarSolicitacaoAssinaturaEstagio(){
		try{
			if(!getTextoNossoNumero().contains("(") || !getTextoNossoNumero().contains(")")) {
				throw new Exception("Necessário colocar os codigos do Estagio dentro de parenteses. Ex: ('estagio1', 'estagio2')");
			}
			getFacadeFactory().getEstagioFacade().realizarCorrecaoBancoDadosPorEstagioCancelado(getTextoNossoNumero(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void processarEstagioSituacaoAguardandoParaRealizando(){
		try{
			if(!getTextoNossoNumero().contains(";")) {
				throw new Exception("Necessário separar os codigos do DocumentoAssinado por PONTO E VIRGULA (;). Ex: estagio1;estagio2");
			}
			getFacadeFactory().getEstagioFacade().processarEstagioSituacaoAguardandoParaRealizando(getTextoNossoNumero(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public synchronized void correcaoNomeClassroom() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().correcaoNomeClassroom(getUsuarioLogadoClone());
			setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public synchronized void correcaoDonoGoogleMeet() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().correcaoDonoMeet(getUsuarioLogadoClone());
			setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getMat() {
		if (mat == null) {
			mat = "";
		}
		return mat;
	}

	public void setMat(String mat) {
		this.mat = mat;
	}

	public Integer getConvenio() {
		if (convenio == null) {
			convenio = 0;
		}
		return convenio;
	}

	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	public Integer getParceiro() {
		if (parceiro == null) {
			parceiro = 0;
		}
		return parceiro;
	}

	public void setParceiro(Integer parceiro) {
		this.parceiro = parceiro;
	}

	public String getTextoNossoNumero() {
		if (textoNossoNumero == null) {
			textoNossoNumero = "";
		}
		return textoNossoNumero;
	}

	public void setTextoNossoNumero(String textoNossoNumero) {
		this.textoNossoNumero = textoNossoNumero;
	}

	public Boolean getConsiderarDataFutura() {
		if (considerarDataFutura == null) {
			considerarDataFutura = true;
		}
		return considerarDataFutura;
	}

	public void setConsiderarDataFutura(Boolean considerarDataFutura) {
		this.considerarDataFutura = considerarDataFutura;
	}
	
	public synchronized void liberarListaConfiguracaoFinanceiraMemoria() {
		try {
			getAplicacaoControle().liberarListaConfiguracaoFinanceiraMemoria();
			setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {

		}
	}
	
	public synchronized void liberarListaConfiguracaoGeralMemoria() {
		try {
			getAplicacaoControle().liberarListaConfiguracaoGeralMemoria();
			setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {

		}
	}
	
	public synchronized void liberarListaConfiguracaoAcademicaMemoria() {
		getAplicacaoControle().liberarListaConfiguracaoAcademicaMemoria();
		setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);	
	}
	
	public synchronized void liberarListaContaCorrenteMemoria() {
		getAplicacaoControle().liberarListaContaCorrenteMemoria();
		setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);	
	}
	
	public synchronized void liberarListaCentroReceitaMemoria() {
		getAplicacaoControle().liberarListaCentroReceitaMemoria();
		setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);	
	}
	
	public synchronized void liberarListaCentroResultadoMemoria() {
		getAplicacaoControle().liberarListaCentroResultadoMemoria();
		setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);	
	}
	
	public synchronized void liberarListaCategoriaDespesaMemoria() {
		getAplicacaoControle().liberarListaCategoriaDespesaMemoria();
		setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);	
	}

	public synchronized void liberarListaCidadeMemoria() {
		getAplicacaoControle().liberarListaCidadeMemoria();
		setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);	
	}

	public Boolean getConsiderarPeriodoVencimento() {
		if (considerarPeriodoVencimento == null) {
			considerarPeriodoVencimento = false;
		}
		return considerarPeriodoVencimento;
	}

	public void setConsiderarPeriodoVencimento(Boolean considerarPeriodoVencimento) {
		this.considerarPeriodoVencimento = considerarPeriodoVencimento;
	}

	public Date getDataInicioPeriodoVencimento() {
		return dataInicioPeriodoVencimento;
	}

	public void setDataInicioPeriodoVencimento(Date dataInicioPeriodoVencimento) {
		this.dataInicioPeriodoVencimento = dataInicioPeriodoVencimento;
	}

	public Date getDataFimPeriodoVencimento() {
		return dataFimPeriodoVencimento;
	}

	public void setDataFimPeriodoVencimento(Date dataFimPeriodoVencimento) {
		this.dataFimPeriodoVencimento = dataFimPeriodoVencimento;
	}
	
	public void desabilitarConsiderarDataFutura() {
		if(getConsiderarPeriodoVencimento()) {
			this.setConsiderarDataFutura(Boolean.FALSE);
		}
	}
	public void desabilitarConsiderarPeriodoVencimento() {
		if (this.getConsiderarDataFutura()) {
			this.setConsiderarPeriodoVencimento(Boolean.FALSE);
		}
	}
	
	public void criarLayoutPadraohistorico() {
		getAplicacaoControle().realizarInclusaoLayoutPadraoHistorico();
	}
	
	public void criarLayoutPadraoAtaResultadosFinais() {
		getAplicacaoControle().realizarInclusaoLayoutPadraoAtaResultadosFinais();
	}
	private Date dataInicioCorrecaoEncode;
	private Date dataFimCorrecaoEncode;
	
	public Date getDataInicioCorrecaoEncode() {
		if(dataInicioCorrecaoEncode == null) {
			try {
				dataInicioCorrecaoEncode =  Uteis.getData("2021-12-14", "yyyy-MM-dd");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataInicioCorrecaoEncode;
	}

	public void setDataInicioCorrecaoEncode(Date dataInicioCorrecaoEncode) {
		this.dataInicioCorrecaoEncode = dataInicioCorrecaoEncode;
	}

	public Date getDataFimCorrecaoEncode() {
		if(dataFimCorrecaoEncode == null) {
			try {
				dataFimCorrecaoEncode =  Uteis.getData("2021-12-18", "yyyy-MM-dd");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dataFimCorrecaoEncode;
	}

	public void setDataFimCorrecaoEncode(Date dataFimCorrecaoEncode) {
		this.dataFimCorrecaoEncode = dataFimCorrecaoEncode;
	}

	public void corrigirEncondingEstagio() {
		try {
		getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().corrigirEncondingEstagio(getDataInicioCorrecaoEncode(), getDataFimCorrecaoEncode(), getUsuarioLogado());
		setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		//new String(string.getBytes(), Charset.forName("UTF-8"));
		
	}

	public String getTextoRequerimento() {
		if(textoRequerimento == null ) {
			textoRequerimento = "" ;
		}
		return textoRequerimento;
	}

	public void setTextoRequerimento(String textoRequerimento) {
		this.textoRequerimento = textoRequerimento;
	}
	
	public void processarSolicitacaoTransferenciaPorRequerimento() {
		
		try{
					
			
			JobProcessaRequerimentoTransferenciaNovaMatricula job = new JobProcessaRequerimentoTransferenciaNovaMatricula(getTextoRequerimento(), getConfiguracaoGeralPadraoSistema() ,getConfiguracaoFinanceiroPadraoSistema() , getUsuarioLogado());
			 Thread th = new Thread(job);
			 th.start();
			 while(th.isAlive()) {
				 Thread.sleep(5000);
			 }
			 if(!job.getConsistirException().getListaMensagemErro().isEmpty()) {
				 
                 setConsistirExceptionMensagemDetalhada("msg_erro", job.getConsistirException(), Uteis.ERRO);			 
			 }else {				 
				 setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			 }

		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	public void limparUnidadeEnsinoMemoria() {
		try {
		getAplicacaoControle().removerUnidadeEnsino(-1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparPerfilAcessoMemoria() {
		try {
		getAplicacaoControle().removerPerfilAcesso(-1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparFormaPagamentoMemoria() {
		try {
		getAplicacaoControle().removerFormaPagamento(-1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void limparOperadoraCartaoMemoria() {
		try {
		getAplicacaoControle().removerOperadoraCartao(-1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void limparConfiguracaoFinanceiraCartaoMemoria() {
		try {
		getAplicacaoControle().removerConfiguracaoFinanceiroCartao(-1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void limparConfiguracaoRecebimentoCartaoMemoria() {
		try {
		getAplicacaoControle().removerConfiguracaoRecebimentoCartaoOnline(-1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void limparDisciplinaMemoria() {
		try {
		getAplicacaoControle().removerDisciplina(-1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparTurmaOfertadaMemoria() {
		try {
			getAplicacaoControle().obterAdicionarRemoverTurmaOfertada(null, null, false, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparProgressBarAssinaturaXmlMemoria() {
		getAplicacaoControle().limparAplicacaoMapDocumentoAssinarXml();
		getAplicacaoControle().getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
		getAplicacaoControle().getProgressBarAssinarXmlMec().encerrar();
	}

	public void limparFoldersDocumentosTechCertMemoria() {
		try {
			getAplicacaoControle().removerFoldersDocumentosTechCert("-1");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private String listaUsuarioAluno;
	
	public String getListaUsuarioAluno() {
		if(listaUsuarioAluno == null) {
			listaUsuarioAluno =  "";
		}
		return listaUsuarioAluno;
	}

	public void setListaUsuarioAluno(String listaUsuarioAluno) {
		this.listaUsuarioAluno = listaUsuarioAluno;
	}
	
	
	public void liberarDadosUsuarioAlunoMemoria() {
		getAplicacaoControle().getMatriculaAlunoCache().clear();
		getAplicacaoControle().getMatriculasAlunoCache().clear();
		getAplicacaoControle().getMatriculaAvaliacaoInstitucionalCache().clear();
		getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().clear();
		getAplicacaoControle().getMatriculaPeriodoAlunoCache().clear();
		getAplicacaoControle().getMatriculaPeriodoTurmaDisciplinasCache().clear();
		getAplicacaoControle().getAnoSemestreMatriculaCache().clear();
		getAplicacaoControle().getUsuarioAlunoPorEmailCache().clear();
		getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().clear();
		getAplicacaoControle().getCalendarioProjetoIntegradorCache().clear();
		getAplicacaoControle().getCalendarioTccCache().clear();
		getAplicacaoControle().getDashboardEstagioAlunoCache().clear();
		getAplicacaoControle().getDashboardEstagioFacilitadorCache().clear();
		getAplicacaoControle().getDashboardsAlunoCache().clear();
		getAplicacaoControle().getLinksUteisUsuarioCache().clear();
		getAplicacaoControle().getMatriculaEstagioDeferidoCache().clear();
		getAplicacaoControle().getPreferenciaSistemaUsuarioCache().clear();		
		setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
	}
	
	public void inicializarCarregamentoUsuarioAlunoMemoria() {
		try {
			if(getListaUsuarioAluno().trim().isEmpty()) {
				throw new Exception("Deve ser informado a lista de código do usuário separados por virgula.");
			}
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().setUsuarioVO(getUsuarioLogado());
			getProgressBarVO().setMaxValue(getListaUsuarioAluno().split(",").length);
			getProgressBarVO().iniciar(0l, getProgressBarVO().getMaxValue(), "Iniciando Dados....", true, this, "realizarCarregamentoMemoriaPortalAluno");
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarTodosUsuariosAlunosAtivos() {
		setListaUsuarioAluno(getFacadeFactory().getUsuarioFacade().realizarCarregamentoListaUsuarioAlunoAtivos());
	}
	
	private ProgressBarVO progressBarVO;
	
	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	
	public void realizarInterrupcaoProcessamento() {
		getProgressBarVO().setForcarEncerramento(true);
	}

	public void realizarCarregamentoMemoriaPortalAluno() {		
		ConsistirException consistirException =  new ConsistirException();
				
		final List<String> mapUsuario = Arrays.asList(getListaUsuarioAluno().split(","));
		ProcessarParalelismo.executarProgressBarVO(0, mapUsuario.size(), 50, getProgressBarVO(), "Carregando dados...", consistirException, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {
				String cod = mapUsuario.get(i);
				try {
				if(!getProgressBarVO().getForcarEncerramento()) {
//					getProgressBarVO().setStatus("Processando Usuario "+getProgressBarVO().getProgresso()+" de "+getProgressBarVO().getMaxValue());
					getFacadeFactory().getUsuarioFacade().realizarCarregamentoDadosUsuarioAlunoAtivo(Integer.valueOf(cod), getProgressBarVO());
//					getProgressBarVO().incrementar();
				}
				}catch (Exception e) {
					consistirException.adicionarListaMensagemErro(e.getMessage());
				}
			}
		});
		if(!consistirException.getListaMensagemErro().isEmpty()) {
			getProgressBarVO().getSuperControle().setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
		}
		getProgressBarVO().setForcarEncerramento(true);
	}

}
