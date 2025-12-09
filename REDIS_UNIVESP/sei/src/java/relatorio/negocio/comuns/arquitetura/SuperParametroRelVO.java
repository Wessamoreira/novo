package relatorio.negocio.comuns.arquitetura;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import controle.arquitetura.LoginControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperArquitetura;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author brethener
 */
public class SuperParametroRelVO extends SuperArquitetura {

	private HashMap parametros = new HashMap();
	private List listaObjetos;
	private TipoRelatorioEnum tipoRelatorioEnum;

	public SuperParametroRelVO() {
		limparParametros();
	}

	public void limparParametros() {
		parametros = new HashMap();
		parametros.put("logoCliente", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logo.png");
		
		
		if (FacesContext.getCurrentInstance() != null) {
			LoginControle loginControle = (LoginControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LoginControle");
			if (loginControle != null && !loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio().trim().isEmpty()) {				
				File imagem = new File(loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio());
				if(imagem.exists()){
					parametros.put("logoPadraoRelatorio", loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio());
				}else{
					parametros.put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
				}
			}else{
				parametros.put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
			}
		}else{
			parametros.put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
		}
		parametros.put("topoHistoricoRel", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "topoHistoricoRel.png");
		parametros.put("rodapeHistoricoRel", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "rodapeHistoricoRel.png");
		parametros.put("logoPadraoRelatorioIdentificacaoEstudantil", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorioIdentificacaoEstudantil.png");
	}

	public TipoRelatorioEnum getTipoRelatorioEnum() {
		if (tipoRelatorioEnum == null) {
			tipoRelatorioEnum = TipoRelatorioEnum.PDF;
		}
		return tipoRelatorioEnum;
	}

	public void setTipoRelatorioEnum(TipoRelatorioEnum tipoRelatorioEnum) {
		this.tipoRelatorioEnum = tipoRelatorioEnum;
	}

	public String getNomeRelatorio() {
		return String.valueOf(new Date().getTime());
	}

	public String getNomeDesignIreport() {
		return (String) parametros.get("nomeDesignIreport");
	}

	public void setNomeDesignIreport(String nomeDesignIreport) {
		parametros.put("nomeDesignIreport", nomeDesignIreport);
	}
	
	public String getNomeEspecificoRelatorio() {
		return (String) parametros.get("nomeEspecificoRelatorio");
	}
	
	public void setNomeEspecificoRelatorio(String NomeEspecificoRelatorio) {
		parametros.put("nomeEspecificoRelatorio", NomeEspecificoRelatorio);
	}

	public List getListaObjetos() {
		if (listaObjetos == null) {
			listaObjetos = new ArrayList(0);
		}
		return listaObjetos;
	}

	public void setListaObjetos(List listaObjetos) {
		this.listaObjetos = listaObjetos;
	}

	public String getNomeUsuario() {
		return (String) parametros.get("nomeUsuario");
	}

	public void setNomeUsuario(String nomeUsuario) {
		parametros.put("nomeUsuario", nomeUsuario);
	}

	public String getTituloRelatorio() {
		return (String) parametros.get("tituloRelatorio");
	}

	public void setTituloRelatorio(String tituloRelatorio) {
		parametros.put("tituloRelatorio", tituloRelatorio);
	}

	public String getVersaoSoftware() {
		return (String) parametros.get("versaoSoftware");
	}

	public void setVersaoSoftware(String versaoSoftware) {
		parametros.put("versaoSoftware", versaoSoftware);
	}

	public String getSubReport_Dir() {
		return (String) parametros.get("SUBREPORT_DIR");
	}

	public void setSubReport_Dir(String SUBREPORT_DIR) {
		parametros.put("SUBREPORT_DIR", SUBREPORT_DIR);
	}
	
	public String getSubRelatorio_Dir() {
		return (String) parametros.get("SUBRELATORIO_DIR");
	}

	public void setSubRelatorio_Dir(String SUBREPORT_DIR) {
		parametros.put("SUBRELATORIO_DIR", SUBREPORT_DIR);
	}

	public String getFiltros() {
		return (String) parametros.get("filtros");
	}

	public void setFiltros(String filtros) {
		parametros.put("filtros", filtros);
	}

	public String getCaminhoBaseRelatorio() {
		return (String) parametros.get("caminhoBaseRelatorio");
	}

	public void setCaminhoBaseRelatorio(String caminhoBaseRelatorio) {
		parametros.put("caminhoBaseRelatorio", caminhoBaseRelatorio);
	}

	public String getNomeEmpresa() {
		return (String) parametros.get("nomeEmpresa");
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		parametros.put("nomeEmpresa", nomeEmpresa);
	}

	public String getLogoCliente() {
		return (String) parametros.get("logoCliente");
	}

	public String getLogoPadraoRelatorio() {
		return (String) parametros.get("logoPadraoRelatorio");
	}

	public String getTopoHistoricoRel() {
		return (String) parametros.get("topoHistoricoRel");
	}

	public String getRodapeHistoricoRel() {
		return (String) parametros.get("rodapeHistoricoRel");
	}

	public HashMap getParametros() {
		return parametros;
	}

	public void setParametros(HashMap parametros) {
		this.parametros = parametros;
	}

	public Integer getQuantidade() {
		return (Integer) parametros.get("quantidade");
	}

	public void setQuantidade(Integer quantidade) {
		parametros.put("quantidade", quantidade);
	}

	public Integer getQuantidadeCursou() {
		return (Integer) parametros.get("quantidadeCursou");
	}

	public void setQuantidadeCursou(Integer quantidadeCursou) {
		parametros.put("quantidadeCursou", quantidadeCursou);
	}

	public Integer getQuantidadeNCursou() {
		return (Integer) parametros.get("quantidadeNCursou");
	}

	public void setQuantidadeNCursou(Integer quantidadeNCursou) {
		parametros.put("quantidadeNCursou", quantidadeNCursou);
	}

	public Integer getQuantidadeAindaNCursou() {
		return (Integer) parametros.get("quantidadeAindaNCursou");
	}

	public void setQuantidadeAindaNCursou(Integer quantidadeAindaNCursou) {
		parametros.put("quantidadeAindaNCursou", quantidadeAindaNCursou);
	}

	public String getTipoCheque() {
		return (String) parametros.get("tipoCheque");
	}

	public void setTipoCheque(String tipoCheque) {
		parametros.put("tipoCheque", tipoCheque);
	}

	public String getTipoContaReceber() {
		return (String) parametros.get("tipoCheque");
	}

	public void setTipoContaReceber(String tipoContaReceber) {
		parametros.put("tipoContaReceber", tipoContaReceber);
	}

	public String getSituacao() {
		return (String) parametros.get("situacao");
	}

	public void setSituacao(String situacao) {
		parametros.put("situacao", situacao);
	}

	public String getProfessor() {
		return (String) parametros.get("professor");
	}

	public void setProfessor(String professor) {
		parametros.put("professor", professor);
	}

	public String getParceiro() {
		return (String) parametros.get("parceiro");
	}

	public void setParceiro(String parceiro) {
		parametros.put("parceiro", parceiro);
	}

	public String getDataEmissao() {
		return (String) parametros.get("dataEmissao");
	}

	public void setDataEmissao(String dataEmissao) {
		parametros.put("dataEmissao", dataEmissao);
	}

	public String getDataPrevisao() {
		return (String) parametros.get("dataPrevisao");
	}

	public void setDataPrevisao(String dataPrevisao) {
		parametros.put("dataPrevisao", dataPrevisao);
	}

	public String getDataInicio() {
		return (String) parametros.get("dataInicio");
	}

	public void setDataInicio(String dataInicio) {
		parametros.put("dataInicio", dataInicio);
	}

	public String getDataFim() {
		return (String) parametros.get("dataFim");
	}

	public void setDataFim(String dataFim) {
		parametros.put("dataFim", dataFim);
	}
	
	public String getDataInicioCompetencia() {
		return (String) parametros.get("dataInicioCompetencia");
	}

	public void setDataInicioCompetencia(String dataInicioCompetencia) {
		parametros.put("dataInicioCompetencia", dataInicioCompetencia);
	}

	public String getDataFimCompetencia() {
		return (String) parametros.get("dataFimCompetencia");
	}

	public void setDataFimCompetencia(String dataFimCompetencia) {
		parametros.put("dataFimCompetencia", dataFimCompetencia);
	}

	public void setDataProva(String dataProva) {
		parametros.put("dataProva", dataProva);
	}

	public String getDataProva() {
		return (String) parametros.get("dataProva");
	}
	
	public void setDescricaoProva(String descricaoProva) {
		parametros.put("descricaoProva", descricaoProva);
	}
	
	public String getSala() {
		return (String) parametros.get("sala");
	}
	
	public void setSala(String sala) {
		parametros.put("sala", sala);
	}

	public String getDescricaoProva() {
		return (String) parametros.get("descricaoProva");
	}
	
	public String getPeriodo() {
		return (String) parametros.get("periodo");
	}

	public void setPeriodo(String periodo) {
		parametros.put("periodo", periodo);
	}

	public String getUnidadeEnsino() {
		return (String) parametros.get("unidadeEnsino");
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		parametros.put("unidadeEnsino", unidadeEnsino);
	}

	public String getCurso() {
		return (String) parametros.get("curso");
	}

	public void setCurso(String curso) {
		parametros.put("curso", curso);
	}
	
	public JRDataSource getListaDS() {
        return new JRBeanArrayDataSource(getLista().toArray());
    }
	
	public List getLista() {
		return (List) parametros.get("lista");
	}
	
	public void setLista(List lista) {
		parametros.put("lista", lista);
	}
	
	public String getAreaConhecimento() {
		return (String) parametros.get("areaConhecimento");
	}

	public void setAreaConhecimento(String AreaConhecimento) {
		parametros.put("areaConhecimento", AreaConhecimento);
	}

	public String getTurno() {
		return (String) parametros.get("turno");
	}

	public void setTurno(String turno) {
		parametros.put("turno", turno);
	}

	public String getTipoRelatorio() {
		return (String) parametros.get("tipoRelatorio");
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		parametros.put("tipoRelatorio", tipoRelatorio);
	}

	public String getTurma() {
		return (String) parametros.get("turma");
	}

	public void setTurma(String turma) {
		parametros.put("turma", turma);
	}

	public String getOrdenadoPor() {
		return (String) parametros.get("ordenadoPor");
	}

	public void setOrdenadoPor(String ordenadoPor) {
		parametros.put("ordenadoPor", ordenadoPor);
	}

	public String getCategoriaDespesa() {
		return (String) parametros.get("categoriaDespesa");
	}

	public void setCategoriaDespesa(String categoriaDespesa) {
		parametros.put("categoriaDespesa", categoriaDespesa);
	}

	public String getFornecedor() {
		return (String) parametros.get("fornecedor");
	}

	public void setFornecedor(String fornecedor) {
		parametros.put("fornecedor", fornecedor);
	}

	public String getFuncionario() {
		return (String) parametros.get("funcionario");
	}

	public void setFuncionario(String funcionario) {
		parametros.put("funcionario", funcionario);
	}

	public String getBanco() {
		return (String) parametros.get("banco");
	}

	public void setBanco(String banco) {
		parametros.put("banco", banco);
	}

	public String getAluno() {
		return (String) parametros.get("aluno");
	}

	public void setAluno(String aluno) {
		parametros.put("aluno", aluno);
	}

	public void setResponsavelFinanceiro(String responsavelFinanceiro) {
		parametros.put("responsavelFinanceiro", responsavelFinanceiro);
	}

	public String getSecao() {
		return (String) parametros.get("secao");
	}

	public void setSecao(String secao) {
		parametros.put("secao", secao);
	}

	public String getNivelBibliografico() {
		return (String) parametros.get("nivelBibliografico");
	}

	public void setNivelBibliografico(String nivelBibliografico) {
		parametros.put("nivelBibliografico", nivelBibliografico);
	}

	public String getCatalogo() {
		return (String) parametros.get("catalogo");
	}

	public void setCatalogo(String catalogo) {
		parametros.put("catalogo", catalogo);
	}

	public String getBiblioteca() {
		return (String) parametros.get("biblioteca");
	}

	public void setBiblioteca(String biblioteca) {
		parametros.put("biblioteca", biblioteca);
	}

	public String getClassificacaoBibliografica() {
		return (String) parametros.get("classificacaoBibliografica");
	}

	public void setClassificacaoBibliografica(String classificacaoBibliografica) {
		parametros.put("classificacaoBibliografica", classificacaoBibliografica);
	}

	public String getTipoOrigem() {
		return (String) parametros.get("tipoOrigem");
	}

	public void setTipoOrigem(String tipoOrigem) {
		parametros.put("tipoOrigem", tipoOrigem);
	}

	public String getTipoPessoa() {
		return (String) parametros.get("tipoPessoa");
	}

	public void setTipoPessoa(String tipoPessoa) {
		parametros.put("tipoPessoa", tipoPessoa);
	}

	public String getContaCorrente() {
		return (String) parametros.get("contaCorrente");
	}

	public void setContaCorrente(String contaCorrente) {
		parametros.put("contaCorrente", contaCorrente);
	}

	public String getSemestre() {
		return (String) parametros.get("semestre");
	}

	public void setSemestre(String semestre) {
		parametros.put("semestre", semestre);
	}

	public String getAno() {
		return (String) parametros.get("ano");
	}

	public void setAno(String ano) {
		parametros.put("ano", ano);
	}

	public String getProcessoSeletivo() {
		return (String) parametros.get("processoSeletivo");
	}

	public void setProcessoSeletivo(String processoSeletivo) {
		parametros.put("processoSeletivo", processoSeletivo);
	}

	public String getDisciplina() {
		return (String) parametros.get("disciplina");
	}

	public void setDisciplina(String disciplina) {
		parametros.put("disciplina", disciplina);
	}

	public Boolean getComparacao() {
		return (Boolean) parametros.get("comparacao");
	}

	public void setComparacao(Boolean comparacao) {
		parametros.put("comparacao", comparacao);
	}

	public String getMatrizCurricular() {
		return (String) parametros.get("matrizCurricular");
	}

	public void setMatrizCurricular(String matrizCurricular) {
		parametros.put("matrizCurricular", matrizCurricular);
	}

	public String getFormaPagamento() {
		return (String) parametros.get("formaPagamento");
	}

	public void setFormaPagamento(String formaPagamento) {
		parametros.put("formaPagamento", formaPagamento);
	}

	public Boolean getEmprestimoRenovacao() {
		return (Boolean) parametros.get("emprestimoRenovacao");
	}

	public void setEmprestimoRenovacao(Boolean emprestimoRenovacao) {
		parametros.put("emprestimoRenovacao", emprestimoRenovacao);
	}

	public Boolean getDevolucao() {
		return (Boolean) parametros.get("devolucao");
	}

	public void setDevolucao(Boolean devolucao) {
		parametros.put("devolucao", devolucao);
	}

	public String getPeriodoLetivo() {
		return (String) parametros.get("periodoletivo");
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		parametros.put("periodoletivo", periodoLetivo);
	}

	public String getGradeCurricular() {
		return (String) parametros.get("gradeCurricular");
	}

	public void setGradeCurricular(String gradeCurricular) {
		parametros.put("gradeCurricular", gradeCurricular);
	}
	
	public String getMatricula() {
		return (String) parametros.get("matricula");
	}

	public void setMatricula(String matricula) {
		parametros.put("matricula", matricula);
	}

	public String getQtdTurma() {
		return (String) parametros.get("qtdTurma");
	}
	
	public void setQtdTurma(String qtdTurma) {
		parametros.put("qtdTurma", qtdTurma);
	}
	
	public Boolean getExibirPreMatriculaVeterano() {
		return (Boolean) parametros.get("exibirPreMatriculaVeterano");
	}
	
	public void setExibirPreMatriculaVeterano(Boolean exibirPreMatriculaVeterano) {
		parametros.put("exibirPreMatriculaVeterano", exibirPreMatriculaVeterano);
	}
	
	public Boolean getExibirPreMatriculaCalouro() {
		return (Boolean) parametros.get("exibirPreMatriculaCalouro");
	}
	
	public void setExibirPreMatriculaCalouro(Boolean exibirPreMatriculaCalouro) {
		parametros.put("exibirPreMatriculaCalouro", exibirPreMatriculaCalouro);
	}
	
	public Boolean getExibirMatriculaVeterano() {
		return (Boolean) parametros.get("exibirMatriculaVeterano");
	}
	
	public void setExibirMatriculaVeterano(Boolean exibirMatriculaVeterano) {
		parametros.put("exibirMatriculaVeterano", exibirMatriculaVeterano);
	}
	
	public Boolean getExibirMatriculaCalouro() {
		return (Boolean) parametros.get("exibirMatriculaCalouro");
	}
	
	public void setExibirMatriculaCalouro(Boolean exibirMatriculaCalouro) {
		parametros.put("exibirMatriculaCalouro", exibirMatriculaCalouro);
	}
	
	public String getNota() {
		return (String) parametros.get("nota");
	}
	
	public void setNota(String nota) {
		parametros.put("nota", nota);
	}

	public void adicionarParametro(String key, Object value) {
		parametros.put(key, value);
	}
	
	public void setSomenteIsentas(String somenteIsentas) {
		parametros.put("somenteIsentas", somenteIsentas);
	}
        
        public String getSomenteIsentas() {
		return (String) parametros.get("somenteIsentas");
	}	

        public String getAssinaturaDigitalFuncionarioPrimario() {
    		return (String) parametros.get("assinaturaDigitalFuncionarioPrimario");
    	}


    	public void setAssinaturaDigitalFuncionarioPrimario(String assinaturaDigitalFuncionarioPrimario) {
    		parametros.put("assinaturaDigitalFuncionarioPrimario", assinaturaDigitalFuncionarioPrimario);
    	}

    	public String getAssinaturaDigitalFuncionarioSecundario() {
    		return (String) parametros.get("assinaturaDigitalFuncionarioSecundario");
    	}

    	public void setAssinaturaDigitalFuncionarioSecundario(String assinaturaDigitalFuncionarioSecundario) {
    		parametros.put("assinaturaDigitalFuncionarioSecundario", assinaturaDigitalFuncionarioSecundario);
    	}
    	
    	public String getAssinaturaDigitalFuncionarioTerciario() {
    		return (String) parametros.get("assinaturaDigitalFuncionarioTerciario");
    	}

    	public void setAssinaturaDigitalFuncionarioTerciario(String assinaturaDigitalFuncionarioTerciario) {
    		parametros.put("assinaturaDigitalFuncionarioTerciario", assinaturaDigitalFuncionarioTerciario);
    	}
    	
    	public String getApresentarAssinaturaDigitalFuncionarioPrimario() {
    		return (String) parametros.get("apresentarAssinaturaDigitalFuncionarioPrimario");
    	}

    	public void setApresentarAssinaturaDigitalFuncionarioPrimario(String apresentarAssinaturaDigitalFuncionarioPrimario) {
    		parametros.put("apresentarAssinaturaDigitalFuncionarioPrimario", apresentarAssinaturaDigitalFuncionarioPrimario);
    	}
    	
    	public String getApresentarAssinaturaDigitalFuncionarioSecundario() {
    		return (String) parametros.get("apresentarAssinaturaDigitalFuncionarioSecundario");
    	}

    	public void setApresentarAssinaturaDigitalFuncionarioSecundario(String apresentarAssinaturaDigitalFuncionarioSecundario) {
    		parametros.put("apresentarAssinaturaDigitalFuncionarioSecundario", apresentarAssinaturaDigitalFuncionarioSecundario);
    	}
    	
    	public String getApresentarAssinaturaDigitalFuncionarioTerciario() {
    		return (String) parametros.get("apresentarAssinaturaDigitalFuncionarioTerciario");
    	}

    	public void setApresentarAssinaturaDigitalFuncionarioTerciario(String apresentarAssinaturaDigitalFuncionarioTerciario) {
    		parametros.put("apresentarAssinaturaDigitalFuncionarioTerciario", apresentarAssinaturaDigitalFuncionarioTerciario);
    	}
    	
    	public String getApresentarSeloAssinaturaDigital() {
    		return (String) parametros.get("apresentarSeloAssinaturaDigital");
    	}

    	public void setApresentarSeloAssinaturaDigital(String apresentarSeloAssinaturaDigital) {
    		parametros.put("apresentarSeloAssinaturaDigital", apresentarSeloAssinaturaDigital);
    	}
    	
    	public String getSeloAssinaturaDigital() {
    		return (String) parametros.get("seloAssinaturaDigital");
    	}

    	public void setSeloAssinaturaDigital(String seloAssinaturaDigital) {
    		parametros.put("seloAssinaturaDigital", seloAssinaturaDigital);
    	}

        public void adicionarLogoUnidadeEnsinoSelecionada(UnidadeEnsinoVO unidadeEnsinoVO){
		try {
			if (!unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().equals("") && !unidadeEnsinoVO.getNomeArquivoLogoRelatorio().equals("")) {
				File imagem = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
				if(imagem.exists()){
					adicionarParametro("logoPadraoRelatorio", getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio() + File.separator + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
				}else {
					adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
				}
			} else {
				adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
