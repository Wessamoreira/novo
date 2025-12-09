package negocio.facade.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.PerguntaChecklistOrigemVO;
import negocio.comuns.academico.PerguntaItemRespostaOrigemVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemMotivosPadroesEstagioVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.RespostaPerguntaRespostaOrigemVO;
import negocio.comuns.academico.enumeradores.SituacaoQuestionarioRespostaOrigemEnum;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.estagio.MotivosPadroesEstagioVO;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.processosel.PerguntaChecklistVO;
import negocio.comuns.processosel.PerguntaItemVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.QuestionarioRespostaOrigemInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>QuestionarioRespostaOrigemVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>QuestionarioRespostaOrigemVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see QuestionarioRespostaOrigemVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class QuestionarioRespostaOrigem extends ControleAcesso implements QuestionarioRespostaOrigemInterfaceFacade {

	private static final long serialVersionUID = -2273928013557716966L;

	protected static String idEntidade;

    public QuestionarioRespostaOrigem() throws Exception {
        super();
        setIdEntidade("QuestionarioRespostaOrigem");
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarClonePerguntaRespostaOrigemArquivo(QuestionarioRespostaOrigemVO obj, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioVO) throws Exception {
    	File fileOriginal = null;
    	File fileClone= null;
    	Optional<PerguntaRespostaOrigemVO> first = obj.getPerguntaRespostaOrigemVOs().stream().filter(p-> !p.getArquivoRespostaVO().getNome().isEmpty()).findFirst();
    	if(first.isPresent()) {
    		fileOriginal = new File(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + first.get().getArquivoRespostaVO() .getPastaBaseArquivo() + File.separator + first.get().getArquivoRespostaVO().getNome());
    		if (fileOriginal.exists()) {
    			fileClone = new File(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP.getValue());
    			if (!fileClone.exists()) {
    				fileClone.mkdirs();
    			}
    			fileClone = new File(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP.getValue() + File.separator + first.get().getArquivoRespostaVO().getNome());
    			if (!fileClone.exists()) {
    				fileClone.createNewFile();
    			}
        		getFacadeFactory().getArquivoHelper().copiar(fileOriginal, fileClone);
        		first.get().getArquivoRespostaVO().setPastaBaseArquivo(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP.getValue());	
        		first.get().getArquivoRespostaVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP);	
    		}	
    	}
		for (PerguntaRespostaOrigemVO pro : obj.getPerguntaRespostaOrigemVOs()) {
			if(!pro.getListaArquivoVOs().isEmpty()) {
				for (ArquivoVO arquivoOriginal : pro.getListaArquivoVOs()) {
					fileOriginal = new File(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + arquivoOriginal .getPastaBaseArquivo() + File.separator + arquivoOriginal.getNome());
					if (fileOriginal.exists()) {
						fileClone = new File(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP.getValue());
		    			if (!fileClone.exists()) {
		    				fileClone.mkdirs();
		    			}
		    			fileClone = new File(configGeralSistema.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP.getValue() + File.separator + arquivoOriginal.getNome());
		    			if (!fileClone.exists()) {
		    				fileClone.createNewFile();
		    			}
						getFacadeFactory().getArquivoHelper().copiar(fileOriginal, fileClone);
						arquivoOriginal.setPastaBaseArquivo(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP.getValue());
						arquivoOriginal.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.PERGUNTA_RESPOSTA_ORIGEM_TMP);
					}
				}
			}
		}
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(QuestionarioRespostaOrigemVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getCodigo() == 0) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}	
    
    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>QuestionarioRespostaOrigemVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>QuestionarioRespostaOrigemVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
        try {
        	incluir(obj, "questionarioRespostaOrigem", new AtributoPersistencia()
					.add("questionario", obj.getQuestionarioVO())
					.add("planoEnsino", obj.getPlanoEnsinoVO())
					.add("escopo", obj.getEscopo())
					.add("requisicao", obj.getRequisicaoVO())
					.add("estagio", obj.getEstagioVO())
					.add("nrVersao", obj.getNrVersao())
					.add("situacaoQuestionarioRespostaOrigemEnum", obj.getSituacaoQuestionarioRespostaOrigemEnum())
					.add("dataEnvioAnalise", obj.getDataEnvioAnalise()) 
					.add("dataLimiteAnalise", obj.getDataLimiteAnalise()) 
					.add("dataLimiteCorrecao", obj.getDataLimiteCorrecao())
					.add("dataEnvioCorrecao", obj.getDataEnvioCorrecao())
					.add("observacaoFinal", obj.getObservacaoFinal())
					.add("motivo", obj.getMotivo())
					, usuario);
    		obj.setNovoObj(Boolean.TRUE);
            if(Uteis.isAtributoPreenchido(obj.getPerguntaRespostaOrigemVOs())) {
	            int index = 1;
	            for (PerguntaRespostaOrigemVO perguntaRespostaOrigemVO : obj.getPerguntaRespostaOrigemVOs()) {
	            	perguntaRespostaOrigemVO.setQuestionarioRespostaOrigemVO(obj);
	            	perguntaRespostaOrigemVO.setOrdem(index);
	            	getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().incluir(perguntaRespostaOrigemVO, usuario);         	
	            	index ++;
				}     
            }
            if (obj.getEstagioVO().getTipoEstagio().isTipoObrigatorioAproveitamento() 
            		|| obj.getEstagioVO().getTipoEstagio().isTipoObrigatorioEquivalencia()) {
            	getFacadeFactory().getQuestionarioRespostaOrigemMotivosPadroesEstagioFacade().persistir(obj.getQuestionarioRespostaOrigemMotivosPadroesEstagioVOs(), usuario);
            }
        } catch (Exception e) {
        	obj.setCodigo(0);
    		obj.setNovoObj(true);
    		obj.getPerguntaRespostaOrigemVOs().stream().forEach(p-> {
    			p.setCodigo(0);
    			p.setNovoObj(true);
    			p.getArquivoRespostaVO().setCodigo(0);
    			p.getArquivoRespostaVO().setNovoObj(true);
    			p.getRespostaPerguntaVO().setCodigo(0);
    			p.getRespostaPerguntaVO().setNovoObj(true);
    			p.getPerguntaItemRespostaOrigemVOs().stream().forEach(pp->{
    				pp.setCodigo(0);
    				pp.setNovoObj(true);
    				});
    			p.getRespostaPerguntaRespostaOrigemVOs().stream().forEach(pp->{
    				pp.setCodigo(0);
    				pp.setNovoObj(true);
    				});
    			p.getListaPerguntaChecklistOrigem().stream().forEach(pp->{
    				pp.setCodigo(0);
    				pp.setNovoObj(true);
    				});
    			});
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>QuestionarioRespostaOrigemVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>QuestionarioRespostaOrigemVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
        try {           
        	alterar(obj, "questionarioRespostaOrigem", new AtributoPersistencia()
        			.add("planoEnsino", obj.getPlanoEnsinoVO())
					.add("escopo", obj.getEscopo())
					.add("requisicao", obj.getRequisicaoVO())
					.add("estagio", obj.getEstagioVO())
					.add("nrVersao", obj.getNrVersao())
					.add("situacaoQuestionarioRespostaOrigemEnum", obj.getSituacaoQuestionarioRespostaOrigemEnum())
					.add("dataEnvioAnalise", obj.getDataEnvioAnalise()) 
					.add("dataLimiteAnalise", obj.getDataLimiteAnalise()) 
					.add("dataLimiteCorrecao", obj.getDataLimiteCorrecao())
					.add("dataEnvioCorrecao", obj.getDataEnvioCorrecao())
					.add("observacaoFinal", obj.getObservacaoFinal())
					.add("motivo", obj.getMotivo())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario); 
            if(Uteis.isAtributoPreenchido(obj.getPerguntaRespostaOrigemVOs())) {
	            int index = 1;
	            for (PerguntaRespostaOrigemVO perguntaRespostaOrigemVO : obj.getPerguntaRespostaOrigemVOs()) {    
	            	perguntaRespostaOrigemVO.setQuestionarioRespostaOrigemVO(obj);
	            	perguntaRespostaOrigemVO.setOrdem(index);
	            	getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().alterar(perguntaRespostaOrigemVO, usuario);               	
	            	index ++;
				}  
            }
            if (obj.getEstagioVO().getTipoEstagio().isTipoObrigatorioAproveitamento() 
            		|| obj.getEstagioVO().getTipoEstagio().isTipoObrigatorioEquivalencia()) {
            	getFacadeFactory().getQuestionarioRespostaOrigemMotivosPadroesEstagioFacade().persistir(obj.getQuestionarioRespostaOrigemMotivosPadroesEstagioVOs(), usuario);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoQuestionarioRespostaOrigemVO(QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) throws Exception {
		try {
			alterar(obj, "questionarioRespostaOrigem", new AtributoPersistencia()
					.add("situacaoQuestionarioRespostaOrigemEnum", obj.getSituacaoQuestionarioRespostaOrigemEnum())
					.add("motivo", obj.getMotivo())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);	
		} catch (Exception e) {
			throw e;
		}
	}
	
    
    public QuestionarioRespostaOrigemVO preencherQuestionarioRespostaOrigem(QuestionarioVO questionarioVO, UsuarioVO usuario) {
    	QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO = new QuestionarioRespostaOrigemVO();    	
    	questionarioRespostaOrigemVO.setQuestionarioVO(questionarioVO);
    	realizarCriacaoPerguntaRespostaOrigemVO(questionarioRespostaOrigemVO);
    	return questionarioRespostaOrigemVO;
    	
    }
    
    private void realizarCriacaoPerguntaRespostaOrigemVO(QuestionarioRespostaOrigemVO questionarioRespostaOrigemVO){
    	for(PerguntaQuestionarioVO perguntaQuestionarioVO: questionarioRespostaOrigemVO.getQuestionarioVO().getPerguntaQuestionarioVOs()) {
    		PerguntaRespostaOrigemVO perguntaRespostaOrigemVO = new PerguntaRespostaOrigemVO();
    		perguntaRespostaOrigemVO.setQuestionarioRespostaOrigemVO(questionarioRespostaOrigemVO);
    		perguntaRespostaOrigemVO.setPerguntaQuestionarioVO(perguntaQuestionarioVO);
    		perguntaRespostaOrigemVO.setPerguntaVO(perguntaQuestionarioVO.getPergunta());
    		perguntaRespostaOrigemVO.setPerguntaItemRespostaOrigemAdicionadaVOs(new ArrayList<List<PerguntaItemRespostaOrigemVO>>(0));
    		questionarioRespostaOrigemVO.getPerguntaRespostaOrigemVOs().add(perguntaRespostaOrigemVO);
    		perguntaRespostaOrigemVO.setRespostaPerguntaRespostaOrigemVOs(realizarCriacaoRespostaPerguntaRespostaOrigemVO(perguntaQuestionarioVO.getPergunta(), perguntaRespostaOrigemVO));
    		//realizarCriacaoRespostaPerguntaRespostaOrigemVO(perguntaQuestionarioVO.getPergunta(), perguntaRespostaOrigemVO);    		
    		realizarCriacaoPerguntaItemRespostaOrigemVO(perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getPergunta(), perguntaRespostaOrigemVO);
    		
    	}
    }
    
    private List<RespostaPerguntaRespostaOrigemVO> realizarCriacaoRespostaPerguntaRespostaOrigemVO(PerguntaVO perguntaVO, PerguntaRespostaOrigemVO perguntaRespostaOrigemVO){
    	List<RespostaPerguntaRespostaOrigemVO> respostaPerguntaRespostaOrigemVOs =  new ArrayList<RespostaPerguntaRespostaOrigemVO>(0);
    	for(RespostaPerguntaVO respostaPerguntaVO : perguntaVO.getRespostaPerguntaVOs()) {
			RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO =  new RespostaPerguntaRespostaOrigemVO();
			respostaPerguntaRespostaOrigemVO.setPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO);
			respostaPerguntaRespostaOrigemVO.setRespostaPerguntaVO(respostaPerguntaVO);
			respostaPerguntaRespostaOrigemVOs.add(respostaPerguntaRespostaOrigemVO);
		}
    	return respostaPerguntaRespostaOrigemVOs;
    }
    
    private void realizarCriacaoPerguntaItemRespostaOrigemVO(PerguntaVO perguntaVO, PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO){
    	for(PerguntaItemVO perguntaItemVO : perguntaVO.getPerguntaItemVOs()) {
    		PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO =  new PerguntaItemRespostaOrigemVO();
    		perguntaItemRespostaOrigemVO.setPerguntaRespostaOrigemPrincipalVO(perguntaRespostaOrigemPrincipalVO);
    		perguntaItemRespostaOrigemVO.setPerguntaItemVO(perguntaItemVO);
    		PerguntaRespostaOrigemVO perguntaRespostaOrigemVO = new PerguntaRespostaOrigemVO();   
    		perguntaRespostaOrigemVO.setPerguntaVO(perguntaItemRespostaOrigemVO.getPerguntaItemVO().getPerguntaVO());
    		realizarCriacaoRespostaPerguntaRespostaOrigemVO(perguntaItemRespostaOrigemVO.getPerguntaItemVO().getPerguntaVO(), perguntaRespostaOrigemVO);
    		perguntaRespostaOrigemVO.setRespostaPerguntaRespostaOrigemVOs(realizarCriacaoRespostaPerguntaRespostaOrigemVO(perguntaItemRespostaOrigemVO.getPerguntaItemVO().getPerguntaVO(), perguntaRespostaOrigemVO));
    		perguntaItemRespostaOrigemVO.setPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO);
    		perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs().add(perguntaItemRespostaOrigemVO);
		}    	
    }
    
    @Override
    public void preencherPerguntaChecklistOrigemVO(QuestionarioRespostaOrigemVO obj, UsuarioVO usuario) {
    	for (PerguntaRespostaOrigemVO pro : obj.getPerguntaRespostaOrigemVOs()) {
    		for (PerguntaChecklistVO pcl : pro.getPerguntaVO().getListaPerguntaChecklistVO()) {
    			if(pcl.getStatusAtivoInativoEnum().isAtivo() && pro.getListaPerguntaChecklistOrigem().stream().noneMatch(p-> p.getPerguntaChecklistVO().getCodigo().equals(pcl.getCodigo()))) {
    				PerguntaChecklistOrigemVO pclo = new PerguntaChecklistOrigemVO();
    				pclo.setPerguntaChecklistVO(pcl);
    				pclo.setPerguntaRespostaOrigemVO(pro);
    				pro.getListaPerguntaChecklistOrigem().add(pclo);
    			}
			}
		}
    }

    public void preencherMotivosPadroesEstagioVO(QuestionarioRespostaOrigemVO obj, TipoEstagioEnum tipoEstagio, Boolean retorno, Boolean indeferido, UsuarioVO usuario) {
    	// consultar os motivos para apresentar que ainda nao estão na listagem e preenche a lista no objeto.
    	List<MotivosPadroesEstagioVO> lista = getFacadeFactory().getMotivosPadroesEstagioFacade().consultarMotivosPadroesEstagioUtilizarTipoComponente(tipoEstagio, retorno, indeferido, obj.getQuestionarioRespostaOrigemMotivosPadroesEstagioVOs(), usuario);
		for (MotivosPadroesEstagioVO mot : lista) {
			QuestionarioRespostaOrigemMotivosPadroesEstagioVO quest = new QuestionarioRespostaOrigemMotivosPadroesEstagioVO();
			quest.setMotivosPadroesEstagioVO(mot);
			quest.setQuestionarioRespostaOrigemVO(obj);
			obj.getQuestionarioRespostaOrigemMotivosPadroesEstagioVOs().add(quest);
		}
    }
    
    @Override
    public void adicionarListaPerguntaItemRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO) throws Exception{
    	if(perguntaRespostaOrigemPrincipalVO.getPerguntaQuestionarioVO().getRespostaObrigatoria()) {
    		for(PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO: perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs()) {    			
    			if(perguntaItemRespostaOrigemVO.getPerguntaItemVO().getRespostaObrigatoria()) {
    				getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().validarDadosPerguntaRespostaOrigemVO(perguntaItemRespostaOrigemVO.getPerguntaRespostaOrigemVO());
    			}
    		}
    	}
    	boolean novo =  true;
    	int sequencia = perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemAdicionadaVOs().size() + 1;
    	for(PerguntaItemRespostaOrigemVO perguntaItemRespostaOrigemVO: perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs()) {
			if(perguntaItemRespostaOrigemVO.getOrdem().equals(0)) {
				perguntaItemRespostaOrigemVO.setOrdem(sequencia);
			}else {
				sequencia = perguntaItemRespostaOrigemVO.getOrdem();
				novo =  true;
			}
    	}
    	if(novo) {
    		perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemAdicionadaVOs().add(perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs());
    	}
    	perguntaRespostaOrigemPrincipalVO.setPerguntaItemRespostaOrigemVOs(new ArrayList<PerguntaItemRespostaOrigemVO>());
    	realizarCriacaoPerguntaItemRespostaOrigemVO(perguntaRespostaOrigemPrincipalVO.getPerguntaQuestionarioVO().getPergunta(), perguntaRespostaOrigemPrincipalVO);
    }
    
    @Override
    public void removerListaPerguntaItemRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO, int index) throws Exception{    	
    		perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemAdicionadaVOs().remove(index);
    	
    }
    
    
    public void editarListaPerguntaItemRespostaOrigemAdicionadasVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO, List<PerguntaItemRespostaOrigemVO> listaPerguntaItemRespostaOrigemVOs, UsuarioVO usuario) throws Exception{
    	
    	listaPerguntaItemRespostaOrigemVOs.stream()
    	.sorted((perguntaitemRespostaOrigem1, perguntaitemRespostaOrigem2) -> perguntaitemRespostaOrigem1.getOrdem().compareTo(perguntaitemRespostaOrigem2.getOrdem()));
    	    	
    	Set<Integer> listSetOrdens = listaPerguntaItemRespostaOrigemVOs.stream().map(perguntaitemRespostaOrigem -> perguntaitemRespostaOrigem.getOrdem())
    			.collect(Collectors.toSet());    	
    	
    	for (Integer ordem : listSetOrdens) {
    		
    		List<PerguntaItemRespostaOrigemVO> listPerguntaItemRespostaOrigemVO = new ArrayList<PerguntaItemRespostaOrigemVO>(0);
    		listPerguntaItemRespostaOrigemVO = listaPerguntaItemRespostaOrigemVOs.stream().filter(perguntaitemRespostaOrigem -> ordem.equals(perguntaitemRespostaOrigem.getOrdem()))
    		.collect(Collectors.toList());

			for (int i = 0; i < listPerguntaItemRespostaOrigemVO.size(); i++) { 
				if(listPerguntaItemRespostaOrigemVO.get(i).getPerguntaRespostaOrigemVO().getPerguntaVO().getTipoResposta().equals("ME") || listPerguntaItemRespostaOrigemVO.get(i).getPerguntaRespostaOrigemVO().getPerguntaVO().getTipoResposta().equals("SE")) {
					listPerguntaItemRespostaOrigemVO.get(i).getPerguntaRespostaOrigemVO().setRespostaPerguntaRespostaOrigemVOs(criarRespostaPerguntaResposta(listPerguntaItemRespostaOrigemVO.get(i).getPerguntaItemVO().getPerguntaVO(), listPerguntaItemRespostaOrigemVO.get(i).getPerguntaRespostaOrigemVO(), usuario));
				}	
			}

			  		   
    		perguntaRespostaOrigemVO.getPerguntaItemRespostaOrigemAdicionadaVOs().add(listPerguntaItemRespostaOrigemVO);
        	perguntaRespostaOrigemVO.setPerguntaItemRespostaOrigemVOs(new ArrayList<PerguntaItemRespostaOrigemVO>(0));
            realizarCriacaoPerguntaItemRespostaOrigemVO(perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getPergunta(), perguntaRespostaOrigemVO); 
        	        	
    		
		}
    		
    	realizarCriacaoRespostaPerguntaRespostaOrigemVO(perguntaRespostaOrigemVO.getPerguntaQuestionarioVO().getPergunta(), perguntaRespostaOrigemVO);
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>QuestionarioRespostaOrigemVO</code>.
     */
    public QuestionarioRespostaOrigemVO novo() throws Exception {
    	QuestionarioRespostaOrigem.incluir(getIdEntidade());
    	QuestionarioRespostaOrigemVO obj = new QuestionarioRespostaOrigemVO();
        return obj;
    }
    
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
    	QuestionarioRespostaOrigem.idEntidade = idEntidade;
    }
    
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return QuestionarioRespostaOrigem.idEntidade;
	}
	
	public StringBuilder getSqlConsultaPadrao() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT questionariorespostaorigem.*, ");
		sql.append(" questionario.codigo as \"questionario.codigo\",  ");
		sql.append(" questionario.descricao as \"questionario.descricao\" ");
		sql.append(" from questionariorespostaorigem ");
		sql.append(" inner join  questionario on  questionariorespostaorigem.questionario = questionario.codigo");
		sql.append(" WHERE 1=1 ");
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<QuestionarioRespostaOrigemVO> consultarPorQuestionarioEstagio(Integer estagio, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSqlConsultaPadrao(); 
		sql.append(" and questionariorespostaorigem.estagio = ? order by nrVersao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {estagio });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public QuestionarioRespostaOrigemVO consultarPorQuestionarioUltimaVersaoPorEstagio(Integer estagio,  int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = getSqlConsultaPadrao();
		sql.append(" and questionariorespostaorigem.estagio = ? order by nrversao desc limit 1  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { estagio });
		if (!tabelaResultado.next()) {
			return new QuestionarioRespostaOrigemVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public QuestionarioRespostaOrigemVO consultarPorQuestionarioPlanoEnsino(Integer questionario, Integer planoEnsino, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = getSqlConsultaPadrao();
		sql.append(" and questionariorespostaorigem.questionario = ? and questionariorespostaorigem.planoensino = ?   ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { questionario, planoEnsino });
		if (!tabelaResultado.next()) {
			return new QuestionarioRespostaOrigemVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public QuestionarioRespostaOrigemVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);		
		StringBuilder sql = getSqlConsultaPadrao();
		sql.append(" and questionariorespostaorigem.codigo = ?   ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Questionário Resposta Origem).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<QuestionarioRespostaOrigemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<QuestionarioRespostaOrigemVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	
	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>QuestionarioRespostaOrigemVO</code>.
	 *
	 * @return O objeto da classe <code>QuestionarioRespostaOrigemVO</code> com os dados devidamente montados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static QuestionarioRespostaOrigemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		QuestionarioRespostaOrigemVO obj = new QuestionarioRespostaOrigemVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario.codigo"));
		obj.getQuestionarioVO().setDescricao(dadosSQL.getString("questionario.descricao"));
		obj.getPlanoEnsinoVO().setCodigo(dadosSQL.getInt("planoEnsino"));
		obj.getRequisicaoVO().setCodigo(dadosSQL.getInt("requisicao"));
		obj.getEstagioVO().setCodigo(dadosSQL.getInt("estagio"));
		obj.setEscopo(EscopoPerguntaEnum.valueOf(dadosSQL.getString("escopo")));
		obj.setSituacaoQuestionarioRespostaOrigemEnum(SituacaoQuestionarioRespostaOrigemEnum.valueOf(dadosSQL.getString("situacaoQuestionarioRespostaOrigemEnum")));
		obj.setNrVersao(dadosSQL.getInt("nrVersao"));
		obj.setObservacaoFinal(dadosSQL.getString("observacaoFinal"));
		obj.setMotivo(dadosSQL.getString("motivo"));
		obj.setDataLimiteAnalise(dadosSQL.getDate("dataLimiteAnalise"));
		obj.setDataLimiteCorrecao(dadosSQL.getDate("dataLimiteCorrecao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;			
		}
		obj.setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioOrigem(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setQuestionarioRespostaOrigemMotivosPadroesEstagioVOs(getFacadeFactory().getQuestionarioRespostaOrigemMotivosPadroesEstagioFacade().consultarPorQuestionarioOrigem(obj.getCodigo(), nivelMontarDados, usuario));
		return obj;
	}
	
    private List<RespostaPerguntaRespostaOrigemVO> criarRespostaPerguntaResposta(PerguntaVO perguntaVO, PerguntaRespostaOrigemVO perguntaRespostaOrigemVO, UsuarioVO usuario) throws Exception{
    	List<RespostaPerguntaRespostaOrigemVO> respostaPerguntaRespostaOrigemVOs =  new ArrayList<RespostaPerguntaRespostaOrigemVO>(0);
    	for(RespostaPerguntaVO respostaPerguntaVO : perguntaVO.getRespostaPerguntaVOs()) {
			RespostaPerguntaRespostaOrigemVO respostaPerguntaRespostaOrigemVO =  new RespostaPerguntaRespostaOrigemVO();
			respostaPerguntaRespostaOrigemVO = getFacadeFactory().getRespostaPerguntaRespostaOrigemInterfaceFacade().consultarPorCodigoRespostaPerguntaCodigoPerguntaRespostaOrigem(respostaPerguntaVO.getCodigo(), perguntaRespostaOrigemVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			respostaPerguntaRespostaOrigemVOs.add(respostaPerguntaRespostaOrigemVO);
		}
    	return respostaPerguntaRespostaOrigemVOs;
    }
		
	
}
