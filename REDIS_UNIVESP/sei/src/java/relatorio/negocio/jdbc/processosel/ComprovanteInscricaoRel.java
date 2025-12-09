/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import relatorio.negocio.comuns.processosel.ComprovanteInscricaoRelVO;
import relatorio.negocio.interfaces.processosel.ComprovanteInscricaoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class ComprovanteInscricaoRel extends SuperRelatorio implements ComprovanteInscricaoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public List<ComprovanteInscricaoRelVO> preencherDadosComprovanteInscricao(List<InscricaoVO> inscricaoVOs, UsuarioVO usuarioVO) throws Exception {
		List<ComprovanteInscricaoRelVO> comprovanteInscricaoRelVOs = new ArrayList<ComprovanteInscricaoRelVO>(0);
		for (InscricaoVO inscricaoVO : inscricaoVOs) {
			ComprovanteInscricaoRelVO obj = new ComprovanteInscricaoRelVO();
			
			obj.setTituloProcessoSeletivo(inscricaoVO.getProcSeletivo().getDescricao());		
			obj.setInscricao(inscricaoVO.getCodigo());
			obj.setSituacaoProcSeletivo(inscricaoVO.getSituacao());
			obj.setFormaIngresso(inscricaoVO.getFormaIngresso());
			obj.setDataInscricao(inscricaoVO.getData_Apresentar());			
			obj.setDataProva(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva_Apresentar());			
			obj.setNomeCandidato(inscricaoVO.getCandidato().getNome());
			obj.setDataNasc(inscricaoVO.getCandidato().getDataNasc_Apresentar());
			obj.setSexo(inscricaoVO.getCandidato().getSexo_Apresentar());			
			obj.setTelefoneRes(inscricaoVO.getCandidato().getTelefoneRes());
			obj.setTelefoneComer(inscricaoVO.getCandidato().getTelefoneComer());
			obj.setCelular(inscricaoVO.getCandidato().getCelular());
			obj.setEmail(inscricaoVO.getCandidato().getEmail());
			obj.setCpf(inscricaoVO.getCandidato().getCPF());
			obj.setRG(inscricaoVO.getCandidato().getRG());
			obj.setOrgaoEmissor(inscricaoVO.getCandidato().getOrgaoEmissor());
			obj.setDataEmissaoRG(inscricaoVO.getCandidato().getDataEmissaoRG_Apresentar());
			obj.setNecessidadesEspeciais(inscricaoVO.getCandidato().getNecessidadesEspeciais());
			obj.setGravida(inscricaoVO.getCandidato().getGravida());
			obj.setCanhoto(inscricaoVO.getCandidato().getCanhoto());		
			obj.setPortadorNecessidadeEspecial(inscricaoVO.getCandidato().getPortadorNecessidadeEspecial());
			obj.setEndereco(inscricaoVO.getCandidato().getEndereco());
			obj.setNumero(inscricaoVO.getCandidato().getNumero());
			obj.setComplemento(inscricaoVO.getCandidato().getComplemento());
			obj.setSetor(inscricaoVO.getCandidato().getSetor());
			obj.setCidade(inscricaoVO.getCandidato().getCidade().getNome());
			obj.setCEP(inscricaoVO.getCandidato().getCEP());
			obj.setEstado(inscricaoVO.getCandidato().getCidade().getEstado().getSigla());
			obj.setNomeCurso(inscricaoVO.getCursoOpcao1().getCurso().getNome());
			obj.setNrPeriodoLetivo(inscricaoVO.getCursoOpcao1().getCurso().getNrPeriodoLetivo());
			obj.setNomeTurno(inscricaoVO.getCursoOpcao1().getTurno().getNome());		
			obj.setSala(inscricaoVO.getSala().getSala());
			if (Uteis.isAtributoPreenchido(inscricaoVO.getSala().getLocalAula())) {
				obj.setLocalProva(inscricaoVO.getSala().getLocalAula().getLocal());
				if (Uteis.isAtributoPreenchido(inscricaoVO.getSala().getLocalAula().getUnidadeEnsino().getCodigo())) {
					UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(inscricaoVO.getSala().getLocalAula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					obj.setEnderecoLocalProva(unidadeEnsinoVO.getEnderecoCompleto());
				} else {
					obj.setEnderecoLocalProva(inscricaoVO.getSala().getLocalAula().getEndereco());
				}			
				}
			try {
				FormacaoAcademicaVO formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorPessoaEEscolaridade(inscricaoVO.getCandidato().getCodigo(), NivelFormacaoAcademica.MEDIO, false, null);
				if(Uteis.isAtributoPreenchido(formacaoAcademica))
				obj.setConcluiuEnsinoMedio(formacaoAcademica.getSituacao().equals("CO"));	
				obj.setInstituicaoEnsinoMedio(formacaoAcademica.getInstituicao());
			} catch (Exception e) {
				obj.setInstituicaoEnsinoMedio("");
			}
			
			if (!Uteis.isAtributoPreenchido(inscricaoVO.getUnidadeEnsino().getConfiguracoes().getCodigo())) {
				inscricaoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(inscricaoVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			}
			obj.setConfiguracaoCandidatoProcessoSeletivoVO(getFacadeFactory().getConfiguracaoCandidatoProcessoSeletivoInterfaceFacade().consultarPorConfiguracaoGeralSistema(inscricaoVO.getUnidadeEnsino().getConfiguracoes().getCodigo(), false,0, usuarioVO));
			comprovanteInscricaoRelVOs.add(obj);
		}
		return comprovanteInscricaoRelVOs;
	}

	public void validarDadosPesquisa(InscricaoVO inscricao) throws ConsistirException {
		if (inscricao.getProcSeletivo().getCodigo().equals(0)) {
			throw new ConsistirException("O campo PROCESSO SELETIVO deve ser informado.");
		}
	}

	public String designIReportRelatorio(String layout) {
		if (layout.equals("LAYOUT_1")) {
			return caminhoBaseRelatorio() + getIdEntidade() + ".jrxml";
		}
		return caminhoBaseRelatorio() + getIdEntidadeLayout2() + ".jrxml";
	}

	public String caminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator;
	}

	public static String getIdEntidade() {
		return "ComprovanteInscricaoRel";
	}

	public static String getIdEntidadeLayout2() {
		return "ComprovanteInscricaoLayout2Rel";
	}
}
