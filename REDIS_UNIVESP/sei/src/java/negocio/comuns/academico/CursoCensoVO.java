/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;


/**
 *
 * @author Danilo
 */
public class CursoCensoVO extends SuperVO {

    private String tipoRegistro;
    private String idCursoInep;
    private String codigoPoloEADINEP;
    private String turnoAluno;
    private String nomeCurso;
    private String codigoAluno;
    private String situacaoVinculo;
    private String dataIngresso;
    private Date  dateIngresso;
    private Date  dataMatricula;
    private Date  dataNasc;
    private String alunoProcedenteEscolaPublica;
    private String vestibular;
    private String enem;
    private String outrosTiposDeSelecao;
    private String pecg;
    private String outrasFormasDeIngresso;
    private String programaReservaVagasAcoesAfirmativas;
    private String programaReservaVagasEtnico;
    private String programaReservaVagasPessoaComDeficiencia;
    private String programaReservaVagasEstudanteProcedenteEnsinoPublico;
    private String programaReservaVagasSocialRendaFamiliar;
    private String programaReservaVagasOutros;
    private String financiamentoEstudantil;
    private String financiamentoEstudantilFIES;
    private String financiamentoEstudantilGovernoEstadual;
    private String financiamentoEstudantilGovernoMunicipal;
    private String financiamentoEstudantilIES;
    private String financiamentoEstudantilEntidadesExternas;
    private String financiamentoEstudantilOutros;
    private String tipoFinanciamentoProUniIntegral;
    private String tipoFinanciamentoProUniParcial;
    private String tipoFinanciamentoNaoReembolsavelProUniIntegral;
    private String tipoFinanciamentoNaoReembolsavelProUniParcial;
    private String tipoFinanciamentoNaoReembolsavelEntidadesExternas;
    private String tipoFinanciamentoNaoReembolsavelGovernoEstadual;
    private String tipoFinanciamentoNaoReembolsavelIES;
    private String tipoFinanciamentoNaoReembolsavelGovernoMunicipal;
    private String tipoFinanciamentoNaoReembolsavelOutros;
    private String apoioSocial;
    private String tipoDeApoioAlimentacao;
    private String tipoDeApoioMoradia;
    private String tipoDeApoioTransporte;
    private String tipoDeApoioMaterialDidatico;
    private String tipoDeApoioBolsaTrabalho;
    private String tipoDeApoioBolsaPermanencia;
    private String atividadeFormacaoComplementar;
    private String atividadeFormacaoComplementarPesquisa;
    private String bolsaRemuneracaoAtivadadeFormacaoPesquisa;
    private String atividadeFormacaoExtensao;
    private String bolsaRemuneracaoAtivadadeFormacaoExtensao;
    private String atividadeFormacaoMonitoria;
    private String bolsaRemuneracaoAtivadadeFormacaoMonitoria;
    private String atividadeFormacaoEstagioNaoObrigatorio;
    private String bolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio;
    private Integer cursoOrigem;
    private String semestreconclusao;
    private Integer anoIngresso;
    private String titulo;
    private Integer cargaHorariaTotalCurso;
    private Integer cargaHorariaIntegralizadaAluno;
    private List<String> listaFinanciamentoEstudantilVOs;
    private String matricula;
    private String formacaoAcademicaTitulo;
    private String justificativaCenso;
    private String mobilidadeAcademica;
    private String tipoMobilidadeAcademica;
    private String mobilidadeAcademicaComplemento;
    
    public static final long serialVersionUID = 1L;
	public String getTipoRegistro() {
		if (tipoRegistro == null) {
			tipoRegistro = "";
		}
		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getIdCursoInep() {
		if (idCursoInep == null) {
			idCursoInep = "";
		}
		return idCursoInep;
	}
	public void setIdCursoInep(String idCursoInep) {
		this.idCursoInep = idCursoInep;
	}
	public String getCodigoPoloEADINEP() {
		if (codigoPoloEADINEP == null) {
			codigoPoloEADINEP = "";
		}
		return codigoPoloEADINEP;
	}
	public void setCodigoPoloEADINEP(String codigoPoloEADINEP) {
		this.codigoPoloEADINEP = codigoPoloEADINEP;
	}
	public String getTurnoAluno() {
		if (turnoAluno == null) {
			turnoAluno = "";
		}
		return turnoAluno;
	}
	public void setTurnoAluno(String turnoAluno) {
		this.turnoAluno = turnoAluno;
	}
	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	public String getCodigoAluno() {
		if (codigoAluno == null) {
			codigoAluno = "";
		}
		return codigoAluno;
	}
	public void setCodigoAluno(String codigoAluno) {
		this.codigoAluno = codigoAluno;
	}
	public String getSituacaoVinculo() {
		if (situacaoVinculo == null) {
			situacaoVinculo = "";
		}
		return situacaoVinculo;
	}
	public void setSituacaoVinculo(String situacaoVinculo) {
		this.situacaoVinculo = situacaoVinculo;
	}
	public String getDataIngresso() {
		if (dataIngresso == null) {
			dataIngresso = "";
		}
		return dataIngresso;
	}
	public void setDataIngresso(String dataIngresso) {
		this.dataIngresso = dataIngresso;
	}
	public Date getDateIngresso() {
		return dateIngresso;
	}
	public void setDateIngresso(Date dateIngresso) {
		this.dateIngresso = dateIngresso;
	}
	public Date getDataMatricula() {
		return dataMatricula;
	}
	public void setDataMatricula(Date dataMatricula) {
		this.dataMatricula = dataMatricula;
	}
	public Date getDataNasc() {
		return dataNasc;
	}
	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}
	public String getAlunoProcedenteEscolaPublica() {
		if (alunoProcedenteEscolaPublica == null) {
			alunoProcedenteEscolaPublica = "";
		}
		return alunoProcedenteEscolaPublica;
	}
	public void setAlunoProcedenteEscolaPublica(String alunoProcedenteEscolaPublica) {
		this.alunoProcedenteEscolaPublica = alunoProcedenteEscolaPublica;
	}
	public String getVestibular() {
		if (vestibular == null) {
			vestibular = "";
		}
		return vestibular;
	}
	public void setVestibular(String vestibular) {
		this.vestibular = vestibular;
	}
	public String getEnem() {
		if (enem == null) {
			enem = "";
		}
		return enem;
	}
	public void setEnem(String enem) {
		this.enem = enem;
	}
	public String getOutrosTiposDeSelecao() {
		if (outrosTiposDeSelecao == null) {
			outrosTiposDeSelecao = "";
		}
		return outrosTiposDeSelecao;
	}
	public void setOutrosTiposDeSelecao(String outrosTiposDeSelecao) {
		this.outrosTiposDeSelecao = outrosTiposDeSelecao;
	}
	public String getPecg() {
		if (pecg == null) {
			pecg = "";
		}
		return pecg;
	}
	public void setPecg(String pecg) {
		this.pecg = pecg;
	}
	public String getOutrasFormasDeIngresso() {
		if (outrasFormasDeIngresso == null) {
			outrasFormasDeIngresso = "";
		}
		return outrasFormasDeIngresso;
	}
	public void setOutrasFormasDeIngresso(String outrasFormasDeIngresso) {
		this.outrasFormasDeIngresso = outrasFormasDeIngresso;
	}
	public String getProgramaReservaVagasAcoesAfirmativas() {
		if (programaReservaVagasAcoesAfirmativas == null) {
			programaReservaVagasAcoesAfirmativas = "";
		}
		return programaReservaVagasAcoesAfirmativas;
	}
	public void setProgramaReservaVagasAcoesAfirmativas(String programaReservaVagasAcoesAfirmativas) {
		this.programaReservaVagasAcoesAfirmativas = programaReservaVagasAcoesAfirmativas;
	}
	public String getProgramaReservaVagasEtnico() {
		if (programaReservaVagasEtnico == null) {
			programaReservaVagasEtnico = "";
		}
		return programaReservaVagasEtnico;
	}
	public void setProgramaReservaVagasEtnico(String programaReservaVagasEtnico) {
		this.programaReservaVagasEtnico = programaReservaVagasEtnico;
	}
	public String getProgramaReservaVagasPessoaComDeficiencia() {
		if (programaReservaVagasPessoaComDeficiencia == null) {
			programaReservaVagasPessoaComDeficiencia = "";
		}
		return programaReservaVagasPessoaComDeficiencia;
	}
	public void setProgramaReservaVagasPessoaComDeficiencia(String programaReservaVagasPessoaComDeficiencia) {
		this.programaReservaVagasPessoaComDeficiencia = programaReservaVagasPessoaComDeficiencia;
	}
	public String getProgramaReservaVagasEstudanteProcedenteEnsinoPublico() {
		if (programaReservaVagasEstudanteProcedenteEnsinoPublico == null) {
			programaReservaVagasEstudanteProcedenteEnsinoPublico = "";
		}
		return programaReservaVagasEstudanteProcedenteEnsinoPublico;
	}
	public void setProgramaReservaVagasEstudanteProcedenteEnsinoPublico(String programaReservaVagasEstudanteProcedenteEnsinoPublico) {
		this.programaReservaVagasEstudanteProcedenteEnsinoPublico = programaReservaVagasEstudanteProcedenteEnsinoPublico;
	}
	public String getProgramaReservaVagasSocialRendaFamiliar() {
		if (programaReservaVagasSocialRendaFamiliar == null) {
			programaReservaVagasSocialRendaFamiliar = "";
		}
		return programaReservaVagasSocialRendaFamiliar;
	}
	public void setProgramaReservaVagasSocialRendaFamiliar(String programaReservaVagasSocialRendaFamiliar) {
		this.programaReservaVagasSocialRendaFamiliar = programaReservaVagasSocialRendaFamiliar;
	}
	public String getProgramaReservaVagasOutros() {
		if (programaReservaVagasOutros == null) {
			programaReservaVagasOutros = "";
		}
		return programaReservaVagasOutros;
	}
	public void setProgramaReservaVagasOutros(String programaReservaVagasOutros) {
		this.programaReservaVagasOutros = programaReservaVagasOutros;
	}
	public String getFinanciamentoEstudantil() {
		if (financiamentoEstudantil == null) {
			financiamentoEstudantil = "";
		}
		return financiamentoEstudantil;
	}
	public void setFinanciamentoEstudantil(String financiamentoEstudantil) {
		this.financiamentoEstudantil = financiamentoEstudantil;
	}
	public String getFinanciamentoEstudantilFIES() {
		if (financiamentoEstudantilFIES == null) {
			financiamentoEstudantilFIES = "";
		}
		return financiamentoEstudantilFIES;
	}
	public void setFinanciamentoEstudantilFIES(String financiamentoEstudantilFIES) {
		this.financiamentoEstudantilFIES = financiamentoEstudantilFIES;
	}
	public String getFinanciamentoEstudantilGovernoEstadual() {
		if (financiamentoEstudantilGovernoEstadual == null) {
			financiamentoEstudantilGovernoEstadual = "";
		}
		return financiamentoEstudantilGovernoEstadual;
	}
	public void setFinanciamentoEstudantilGovernoEstadual(String financiamentoEstudantilGovernoEstadual) {
		this.financiamentoEstudantilGovernoEstadual = financiamentoEstudantilGovernoEstadual;
	}
	public String getFinanciamentoEstudantilGovernoMunicipal() {
		if (financiamentoEstudantilGovernoMunicipal == null) {
			financiamentoEstudantilGovernoMunicipal = "";
		}
		return financiamentoEstudantilGovernoMunicipal;
	}
	public void setFinanciamentoEstudantilGovernoMunicipal(String financiamentoEstudantilGovernoMunicipal) {
		this.financiamentoEstudantilGovernoMunicipal = financiamentoEstudantilGovernoMunicipal;
	}
	public String getFinanciamentoEstudantilIES() {
		if (financiamentoEstudantilIES == null) {
			financiamentoEstudantilIES = "";
		}
		return financiamentoEstudantilIES;
	}
	public void setFinanciamentoEstudantilIES(String financiamentoEstudantilIES) {
		this.financiamentoEstudantilIES = financiamentoEstudantilIES;
	}
	public String getFinanciamentoEstudantilEntidadesExternas() {
		if (financiamentoEstudantilEntidadesExternas == null) {
			financiamentoEstudantilEntidadesExternas = "";
		}
		return financiamentoEstudantilEntidadesExternas;
	}
	public void setFinanciamentoEstudantilEntidadesExternas(String financiamentoEstudantilEntidadesExternas) {
		this.financiamentoEstudantilEntidadesExternas = financiamentoEstudantilEntidadesExternas;
	}
	public String getFinanciamentoEstudantilOutros() {
		if (financiamentoEstudantilOutros == null) {
			financiamentoEstudantilOutros = "";
		}
		return financiamentoEstudantilOutros;
	}
	public void setFinanciamentoEstudantilOutros(String financiamentoEstudantilOutros) {
		this.financiamentoEstudantilOutros = financiamentoEstudantilOutros;
	}
	public String getTipoFinanciamentoProUniIntegral() {
		if (tipoFinanciamentoProUniIntegral == null) {
			tipoFinanciamentoProUniIntegral = "";
		}
		return tipoFinanciamentoProUniIntegral;
	}
	public void setTipoFinanciamentoProUniIntegral(String tipoFinanciamentoProUniIntegral) {
		this.tipoFinanciamentoProUniIntegral = tipoFinanciamentoProUniIntegral;
	}
	public String getTipoFinanciamentoProUniParcial() {
		if (tipoFinanciamentoProUniParcial == null) {
			tipoFinanciamentoProUniParcial = "";
		}
		return tipoFinanciamentoProUniParcial;
	}
	public void setTipoFinanciamentoProUniParcial(String tipoFinanciamentoProUniParcial) {
		this.tipoFinanciamentoProUniParcial = tipoFinanciamentoProUniParcial;
	}
	public String getTipoFinanciamentoNaoReembolsavelProUniIntegral() {
		if (tipoFinanciamentoNaoReembolsavelProUniIntegral == null) {
			tipoFinanciamentoNaoReembolsavelProUniIntegral = "";
		}
		return tipoFinanciamentoNaoReembolsavelProUniIntegral;
	}
	public void setTipoFinanciamentoNaoReembolsavelProUniIntegral(String tipoFinanciamentoNaoReembolsavelProUniIntegral) {
		this.tipoFinanciamentoNaoReembolsavelProUniIntegral = tipoFinanciamentoNaoReembolsavelProUniIntegral;
	}
	public String getTipoFinanciamentoNaoReembolsavelProUniParcial() {
		if (tipoFinanciamentoNaoReembolsavelProUniParcial == null) {
			tipoFinanciamentoNaoReembolsavelProUniParcial = "";
		}
		return tipoFinanciamentoNaoReembolsavelProUniParcial;
	}
	public void setTipoFinanciamentoNaoReembolsavelProUniParcial(String tipoFinanciamentoNaoReembolsavelProUniParcial) {
		this.tipoFinanciamentoNaoReembolsavelProUniParcial = tipoFinanciamentoNaoReembolsavelProUniParcial;
	}
	public String getTipoFinanciamentoNaoReembolsavelEntidadesExternas() {
		if (tipoFinanciamentoNaoReembolsavelEntidadesExternas == null) {
			tipoFinanciamentoNaoReembolsavelEntidadesExternas = "";
		}
		return tipoFinanciamentoNaoReembolsavelEntidadesExternas;
	}
	public void setTipoFinanciamentoNaoReembolsavelEntidadesExternas(String tipoFinanciamentoNaoReembolsavelEntidadesExternas) {
		this.tipoFinanciamentoNaoReembolsavelEntidadesExternas = tipoFinanciamentoNaoReembolsavelEntidadesExternas;
	}
	public String getTipoFinanciamentoNaoReembolsavelGovernoEstadual() {
		if (tipoFinanciamentoNaoReembolsavelGovernoEstadual == null) {
			tipoFinanciamentoNaoReembolsavelGovernoEstadual = "";
		}
		return tipoFinanciamentoNaoReembolsavelGovernoEstadual;
	}
	public void setTipoFinanciamentoNaoReembolsavelGovernoEstadual(String tipoFinanciamentoNaoReembolsavelGovernoEstadual) {
		this.tipoFinanciamentoNaoReembolsavelGovernoEstadual = tipoFinanciamentoNaoReembolsavelGovernoEstadual;
	}
	public String getTipoFinanciamentoNaoReembolsavelIES() {
		if (tipoFinanciamentoNaoReembolsavelIES == null) {
			tipoFinanciamentoNaoReembolsavelIES = "";
		}
		return tipoFinanciamentoNaoReembolsavelIES;
	}
	public void setTipoFinanciamentoNaoReembolsavelIES(String tipoFinanciamentoNaoReembolsavelIES) {
		this.tipoFinanciamentoNaoReembolsavelIES = tipoFinanciamentoNaoReembolsavelIES;
	}
	public String getTipoFinanciamentoNaoReembolsavelGovernoMunicipal() {
		if (tipoFinanciamentoNaoReembolsavelGovernoMunicipal == null) {
			tipoFinanciamentoNaoReembolsavelGovernoMunicipal = "";
		}
		return tipoFinanciamentoNaoReembolsavelGovernoMunicipal;
	}
	public void setTipoFinanciamentoNaoReembolsavelGovernoMunicipal(String tipoFinanciamentoNaoReembolsavelGovernoMunicipal) {
		this.tipoFinanciamentoNaoReembolsavelGovernoMunicipal = tipoFinanciamentoNaoReembolsavelGovernoMunicipal;
	}
	public String getTipoFinanciamentoNaoReembolsavelOutros() {
		if (tipoFinanciamentoNaoReembolsavelOutros == null) {
			tipoFinanciamentoNaoReembolsavelOutros = "";
		}
		return tipoFinanciamentoNaoReembolsavelOutros;
	}
	public void setTipoFinanciamentoNaoReembolsavelOutros(String tipoFinanciamentoNaoReembolsavelOutros) {
		this.tipoFinanciamentoNaoReembolsavelOutros = tipoFinanciamentoNaoReembolsavelOutros;
	}
	public String getApoioSocial() {
		if (apoioSocial == null) {
			apoioSocial = "";
		}
		return apoioSocial;
	}
	public void setApoioSocial(String apoioSocial) {
		this.apoioSocial = apoioSocial;
	}
	public String getTipoDeApoioAlimentacao() {
		if (tipoDeApoioAlimentacao == null) {
			tipoDeApoioAlimentacao = "";
		}
		return tipoDeApoioAlimentacao;
	}
	public void setTipoDeApoioAlimentacao(String tipoDeApoioAlimentacao) {
		this.tipoDeApoioAlimentacao = tipoDeApoioAlimentacao;
	}
	public String getTipoDeApoioMoradia() {
		if (tipoDeApoioMoradia == null) {
			tipoDeApoioMoradia = "";
		}
		return tipoDeApoioMoradia;
	}
	public void setTipoDeApoioMoradia(String tipoDeApoioMoradia) {
		this.tipoDeApoioMoradia = tipoDeApoioMoradia;
	}
	public String getTipoDeApoioTransporte() {
		if (tipoDeApoioTransporte == null) {
			tipoDeApoioTransporte = "";
		}
		return tipoDeApoioTransporte;
	}
	public void setTipoDeApoioTransporte(String tipoDeApoioTransporte) {
		this.tipoDeApoioTransporte = tipoDeApoioTransporte;
	}
	public String getTipoDeApoioMaterialDidatico() {
		if (tipoDeApoioMaterialDidatico == null) {
			tipoDeApoioMaterialDidatico = "";
		}
		return tipoDeApoioMaterialDidatico;
	}
	public void setTipoDeApoioMaterialDidatico(String tipoDeApoioMaterialDidatico) {
		this.tipoDeApoioMaterialDidatico = tipoDeApoioMaterialDidatico;
	}
	public String getTipoDeApoioBolsaTrabalho() {
		if (tipoDeApoioBolsaTrabalho == null) {
			tipoDeApoioBolsaTrabalho = "";
		}
		return tipoDeApoioBolsaTrabalho;
	}
	public void setTipoDeApoioBolsaTrabalho(String tipoDeApoioBolsaTrabalho) {
		this.tipoDeApoioBolsaTrabalho = tipoDeApoioBolsaTrabalho;
	}
	public String getTipoDeApoioBolsaPermanencia() {
		if (tipoDeApoioBolsaPermanencia == null) {
			tipoDeApoioBolsaPermanencia = "";
		}
		return tipoDeApoioBolsaPermanencia;
	}
	public void setTipoDeApoioBolsaPermanencia(String tipoDeApoioBolsaPermanencia) {
		this.tipoDeApoioBolsaPermanencia = tipoDeApoioBolsaPermanencia;
	}
	public String getAtividadeFormacaoComplementar() {
		if (atividadeFormacaoComplementar == null) {
			atividadeFormacaoComplementar = "";
		}
		return atividadeFormacaoComplementar;
	}
	public void setAtividadeFormacaoComplementar(String atividadeFormacaoComplementar) {
		this.atividadeFormacaoComplementar = atividadeFormacaoComplementar;
	}
	public String getAtividadeFormacaoComplementarPesquisa() {
		if (atividadeFormacaoComplementarPesquisa == null) {
			atividadeFormacaoComplementarPesquisa = "";
		}
		return atividadeFormacaoComplementarPesquisa;
	}
	public void setAtividadeFormacaoComplementarPesquisa(String atividadeFormacaoComplementarPesquisa) {
		this.atividadeFormacaoComplementarPesquisa = atividadeFormacaoComplementarPesquisa;
	}
	public String getBolsaRemuneracaoAtivadadeFormacaoPesquisa() {
		if (bolsaRemuneracaoAtivadadeFormacaoPesquisa == null) {
			bolsaRemuneracaoAtivadadeFormacaoPesquisa = "";
		}
		return bolsaRemuneracaoAtivadadeFormacaoPesquisa;
	}
	public void setBolsaRemuneracaoAtivadadeFormacaoPesquisa(String bolsaRemuneracaoAtivadadeFormacaoPesquisa) {
		this.bolsaRemuneracaoAtivadadeFormacaoPesquisa = bolsaRemuneracaoAtivadadeFormacaoPesquisa;
	}
	public String getAtividadeFormacaoExtensao() {
		if (atividadeFormacaoExtensao == null) {
			atividadeFormacaoExtensao = "";
		}
		return atividadeFormacaoExtensao;
	}
	public void setAtividadeFormacaoExtensao(String atividadeFormacaoExtensao) {
		this.atividadeFormacaoExtensao = atividadeFormacaoExtensao;
	}
	public String getBolsaRemuneracaoAtivadadeFormacaoExtensao() {
		if (bolsaRemuneracaoAtivadadeFormacaoExtensao == null) {
			bolsaRemuneracaoAtivadadeFormacaoExtensao = "";
		}
		return bolsaRemuneracaoAtivadadeFormacaoExtensao;
	}
	public void setBolsaRemuneracaoAtivadadeFormacaoExtensao(String bolsaRemuneracaoAtivadadeFormacaoExtensao) {
		this.bolsaRemuneracaoAtivadadeFormacaoExtensao = bolsaRemuneracaoAtivadadeFormacaoExtensao;
	}
	public String getAtividadeFormacaoMonitoria() {
		if (atividadeFormacaoMonitoria == null) {
			atividadeFormacaoMonitoria = "";
		}
		return atividadeFormacaoMonitoria;
	}
	public void setAtividadeFormacaoMonitoria(String atividadeFormacaoMonitoria) {
		this.atividadeFormacaoMonitoria = atividadeFormacaoMonitoria;
	}
	public String getBolsaRemuneracaoAtivadadeFormacaoMonitoria() {
		if (bolsaRemuneracaoAtivadadeFormacaoMonitoria == null) {
			bolsaRemuneracaoAtivadadeFormacaoMonitoria = "";
		}
		return bolsaRemuneracaoAtivadadeFormacaoMonitoria;
	}
	public void setBolsaRemuneracaoAtivadadeFormacaoMonitoria(String bolsaRemuneracaoAtivadadeFormacaoMonitoria) {
		this.bolsaRemuneracaoAtivadadeFormacaoMonitoria = bolsaRemuneracaoAtivadadeFormacaoMonitoria;
	}
	public String getAtividadeFormacaoEstagioNaoObrigatorio() {
		if (atividadeFormacaoEstagioNaoObrigatorio == null) {
			atividadeFormacaoEstagioNaoObrigatorio = "";
		}
		return atividadeFormacaoEstagioNaoObrigatorio;
	}
	public void setAtividadeFormacaoEstagioNaoObrigatorio(String atividadeFormacaoEstagioNaoObrigatorio) {
		this.atividadeFormacaoEstagioNaoObrigatorio = atividadeFormacaoEstagioNaoObrigatorio;
	}
	public String getBolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio() {
		if (bolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio == null) {
			bolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio = "";
		}
		return bolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio;
	}
	public void setBolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio(String bolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio) {
		this.bolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio = bolsaRemuneracaoAtivadadeFormacaoEstagioNaoObrigatorio;
	}
	public String getSemestreconclusao() {
		if(semestreconclusao == null){
			semestreconclusao = "";
		}
		return semestreconclusao;
	}
	public void setSemestreconclusao(String semestreconclusao) {
		this.semestreconclusao = semestreconclusao;
	}
	public Integer getAnoIngresso() {
		if (anoIngresso == null) {
			anoIngresso = 0;
		}
		return anoIngresso;
	}
	public void setAnoIngresso(Integer anoIngresso) {
		this.anoIngresso = anoIngresso;
	}
	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Integer getCursoOrigem() {
		if (cursoOrigem == null) {
			cursoOrigem = 0;
		}
		return cursoOrigem;
	}
	public void setCursoOrigem(Integer cursoOrigem) {
		this.cursoOrigem = cursoOrigem;
	}
	public Integer getCargaHorariaTotalCurso() {
		if (cargaHorariaTotalCurso == null) {
			cargaHorariaTotalCurso = 0;
		}
		return cargaHorariaTotalCurso;
	}
	public void setCargaHorariaTotalCurso(Integer cargaHorariaTotalCurso) {
		this.cargaHorariaTotalCurso = cargaHorariaTotalCurso;
	}
	public Integer getCargaHorariaIntegralizadaAluno() {
		if (cargaHorariaIntegralizadaAluno == null) {
			cargaHorariaIntegralizadaAluno = 0;
		}
		return cargaHorariaIntegralizadaAluno;
	}
	public void setCargaHorariaIntegralizadaAluno(Integer cargaHorariaIntegralizadaAluno) {
		this.cargaHorariaIntegralizadaAluno = cargaHorariaIntegralizadaAluno;
	}
	
	public List<String> getListaFinanciamentoEstudantilVOs() {
		if (listaFinanciamentoEstudantilVOs == null) {
			listaFinanciamentoEstudantilVOs = new ArrayList<String>(0);
		}
		return listaFinanciamentoEstudantilVOs;
	}

	public void setListaFinanciamentoEstudantilVOs(List<String> listaFinanciamentoEstudantilVOs) {
		this.listaFinanciamentoEstudantilVOs = listaFinanciamentoEstudantilVOs;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getFormacaoAcademicaTitulo() {
		if (formacaoAcademicaTitulo == null) {
			formacaoAcademicaTitulo = "";
		}
		return formacaoAcademicaTitulo;
	}

	public void setFormacaoAcademicaTitulo(String formacaoAcademicaTitulo) {
		this.formacaoAcademicaTitulo = formacaoAcademicaTitulo;
	}
	
	public String getJustificativaCenso() {
		if (justificativaCenso == null) {
			justificativaCenso = "";
		}
		return justificativaCenso;
	}

	public void setJustificativaCenso(String justificativaCenso) {
		this.justificativaCenso = justificativaCenso;
	}

	public String getMobilidadeAcademica() {
		if (mobilidadeAcademica == null) {
			mobilidadeAcademica = "0";
		}
		return mobilidadeAcademica;
	}

	public void setMobilidadeAcademica(String mobilidadeAcademica) {
		this.mobilidadeAcademica = mobilidadeAcademica;
	}

	public String getTipoMobilidadeAcademica() {
		if (tipoMobilidadeAcademica == null) {
			tipoMobilidadeAcademica = "";
		}
		return tipoMobilidadeAcademica;
	}
	
	public void setTipoMobilidadeAcademica(String tipoMobilidadeAcademica) {
		this.tipoMobilidadeAcademica = tipoMobilidadeAcademica;
	}
	
	public String getMobilidadeAcademicaComplemento() {
		if (mobilidadeAcademicaComplemento == null) {
			mobilidadeAcademicaComplemento = "";
		}
		return mobilidadeAcademicaComplemento;
	}

	public void setMobilidadeAcademicaComplemento(String mobilidadeAcademicaComplemento) {
		this.mobilidadeAcademicaComplemento = mobilidadeAcademicaComplemento;
	}
}
