/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.util.Date;
import negocio.comuns.financeiro.ContaReceberVO;

/**
 * 
 * @author Euripedes
 */
public class InadimplenciaRelVO {

    private String matricula;
    private String responsavelFinanceiro;
    private String nome;
    private String cpf;
    private String setor;
    private String complemento;
    private String numero;
    private String endereco;
    private String telresidencial;
    private String telcelular;
    private String email;
    private String email2;
    private String curso;
    private String turno;
    private String origem;
    private Date dataVencimento;
    private Double valor;
    private String turma;
    private String unidadeEnsino;
    
    //Unidade de Ensino Financeira esta sendo trazida sempre mas sera exibida somente quando selcionada - 2018-01-29 - gilberto.nery
    private String unidadeEnsinoFinanceira;
    
    private Double multa;
    private Double juros;
    private Double desconto;
    private Double total;
    private String parcela;
    private Integer cont;
    private Double valorDiaPrimeiroVencimento;
    private Double valorCheio;
    private String ano;
    private String semestre;
    private String cidade;
    private String estado;
    private String cep;
    private Integer codigoContaReceber;
    private String nossoNumero;
    private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
    private String parceiroNome;
    private String parceiroTelComercial1;
    private String parceiroCep;
    private String parceiroEndereco;
    private String parceiroNumero;
    private String parceiroSetor;
    private String parceiroComplemento;
    private ContaReceberVO contaReceberVO;
    private Double valorFinalDescontosCalculados;
    private Boolean serasa;
    private Boolean trazerValorFinalDescontoCalculado;
    private String idadeAluno;
    private String listaParcelaNotificacaoInadimplente;
    private String agente;


    public InadimplenciaRelVO() {
        setMatricula("");
        setNome("");
        setCurso("");
        setTurma("");
        setTurno("");
        setOrigem("");
        setDataVencimento(new Date());
        setValor(0.0);
        setUnidadeEnsino("");
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Double getJuros() {
        return juros;
    }

    public void setJuros(Double juros) {
        this.juros = juros;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public String getCpf() {
        if (cpf == null) {
            cpf = "";
        }
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelcelular() {
        if (telcelular == null) {
            telcelular = "";
        }
        return telcelular;
    }

    public void setTelcelular(String telcelular) {
        this.telcelular = telcelular;
    }

    public String getTelresidencial() {
        if (telresidencial == null) {
            telresidencial = "";
        }
        return telresidencial;
    }

    public void setTelresidencial(String telresidencial) {
        this.telresidencial = telresidencial;
    }

    public Integer getCont() {
        if (cont == null) {
            cont = 1;
        }
        return cont;
    }

    public void setCont(Integer cont) {
        this.cont = cont;
    }

    public void setValorDiaPrimeiroVencimento(Double valorDiaPrimeiroVencimento) {
        this.valorDiaPrimeiroVencimento = valorDiaPrimeiroVencimento;
    }

    public Double getValorDiaPrimeiroVencimento() {
        if (valorDiaPrimeiroVencimento == null) {
            valorDiaPrimeiroVencimento = 0.0;
        }
        return valorDiaPrimeiroVencimento;
    }

    public void setValorCheio(Double valorCheio) {
        this.valorCheio = valorCheio;
    }

    public Double getValorCheio() {
        if (valorCheio == null) {
            valorCheio = 0.0;
        }
        return valorCheio;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public String getSetor() {
        if (setor == null) {
            setor = "";
        }
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getComplemento() {
        if (complemento == null) {
            complemento = "";
        }
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
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

    public String getEndereco() {
        if (endereco == null) {
            endereco = "";
        }
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCidade() {
        if (cidade == null) {
            cidade = "";
        }
        return cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        if (estado == null) {
            estado = "";
        }
        return estado;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCep() {
        if (cep == null) {
            cep = "";
        }
        return cep;
    }

    public Integer getCodigoContaReceber() {
        if (codigoContaReceber == null) {
            codigoContaReceber = 0;
        }
        return codigoContaReceber;
    }

    public void setCodigoContaReceber(Integer codigoContaReceber) {
        this.codigoContaReceber = codigoContaReceber;
    }

    public String getNossoNumero() {
        if (nossoNumero == null) {
            nossoNumero = "";
        }
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
        if (valorDescontoCalculadoPrimeiraFaixaDescontos == null) {
            valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
        this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public String getParceiroCep() {
        if (parceiroCep == null) {
            parceiroCep = "";
        }
        return parceiroCep;
    }

    public void setParceiroCep(String parceiroCep) {
        this.parceiroCep = parceiroCep;
    }

    public String getParceiroComplemento() {
        if (parceiroComplemento == null) {
            parceiroComplemento = "";
        }
        return parceiroComplemento;
    }

    public void setParceiroComplemento(String parceiroComplemento) {
        this.parceiroComplemento = parceiroComplemento;
    }

    public String getParceiroEndereco() {
        if (parceiroEndereco == null) {
            parceiroEndereco = "";
        }
        return parceiroEndereco;
    }

    public void setParceiroEndereco(String parceiroEndereco) {
        this.parceiroEndereco = parceiroEndereco;
    }

    public String getParceiroNome() {
        if (parceiroNome == null) {
            parceiroNome = "";
        }
        return parceiroNome;
    }

    public void setParceiroNome(String parceiroNome) {
        this.parceiroNome = parceiroNome;
    }

    public String getParceiroNumero() {
        if (parceiroNumero == null) {
            parceiroNumero = "";
        }
        return parceiroNumero;
    }

    public void setParceiroNumero(String parceiroNumero) {
        this.parceiroNumero = parceiroNumero;
    }

    public String getParceiroSetor() {
        if (parceiroSetor == null) {
            parceiroSetor = "";
        }
        return parceiroSetor;
    }

    public void setParceiroSetor(String parceiroSetor) {
        this.parceiroSetor = parceiroSetor;
    }

    public String getParceiroTelComercial1() {
        if (parceiroTelComercial1 == null) {
            parceiroTelComercial1 = "";
        }
        return parceiroTelComercial1;
    }

    public void setParceiroTelComercial1(String parceiroTelComercial1) {
        this.parceiroTelComercial1 = parceiroTelComercial1;
    }

    public String getEmail2() {
        if (email2 == null) {
            email2 = "";
        }
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public ContaReceberVO getContaReceberVO() {
        if (contaReceberVO == null) {
            contaReceberVO = new ContaReceberVO();
        }
        return contaReceberVO;
    }

    public void setContaReceberVO(ContaReceberVO contaReceberVO) {
        this.contaReceberVO = contaReceberVO;
    }

    public Double getValorFinalDescontosCalculados() {
        if (valorFinalDescontosCalculados == null) {
            valorFinalDescontosCalculados = 0.0;
        }
        return valorFinalDescontosCalculados;
    }

    public void setValorFinalDescontosCalculados(Double valorFinalDescontosCalculados) {
        this.valorFinalDescontosCalculados = valorFinalDescontosCalculados;
    }
    
	public Boolean getTrazerValorFinalDescontoCalculado() {
		if (trazerValorFinalDescontoCalculado == null) {
			trazerValorFinalDescontoCalculado = false;
		}
		return trazerValorFinalDescontoCalculado;
	}

	public void setTrazerValorFinalDescontoCalculado(Boolean trazerValorFinalDescontoCalculado) {
		this.trazerValorFinalDescontoCalculado = trazerValorFinalDescontoCalculado;
	}
	
	public String getSerasa_Apresentar() {
		if (getSerasa()) {
			return "Sim";
		}
		return "Não";
	}

	public Boolean getSerasa() {
		if (serasa == null) {
			serasa = Boolean.FALSE;
		}
		return serasa;
	}

	public void setSerasa(Boolean serasa) {
		this.serasa = serasa;
	}

    
    public String getResponsavelFinanceiro() {
        if(responsavelFinanceiro == null){
            responsavelFinanceiro = "";
        }
        return responsavelFinanceiro;
    }

    
    public void setResponsavelFinanceiro(String responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
    }
    public String getIdadeAluno() {
    	if(idadeAluno == null){
    		idadeAluno = "";
    	}
    	return idadeAluno;
	}

	public void setIdadeAluno(String idadeAluno) {
		this.idadeAluno = idadeAluno;
	}
	public String getListaParcelaNotificacaoInadimplente() {
		if (listaParcelaNotificacaoInadimplente == null) {
			listaParcelaNotificacaoInadimplente = "";
		}
		return listaParcelaNotificacaoInadimplente;
	}

	public void setListaParcelaNotificacaoInadimplente(String listaParcelaNotificacaoInadimplente) {
		this.listaParcelaNotificacaoInadimplente = listaParcelaNotificacaoInadimplente;
	}

	public String getUnidadeEnsinoFinanceira() {
		if (unidadeEnsinoFinanceira == null)
			unidadeEnsinoFinanceira = "";
		return unidadeEnsinoFinanceira;
	}

	public void setUnidadeEnsinoFinanceira(String unidadeEnsinoFinanceira) {
		this.unidadeEnsinoFinanceira = unidadeEnsinoFinanceira;
	}

	public String getAgente() {
		if (agente == null) {
			agente = "";
		}
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}
}