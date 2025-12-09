CREATE OR REPLACE FUNCTION public.fn_validarsituacaocontapagar(p_contapagar integer, p_situacao text)
 RETURNS BOOLEAN
 LANGUAGE plpgsql
AS $function$
DECLARE 
  v_retorno BOOLEAN := FALSE;
BEGIN 
    IF (p_situacao = 'PA' AND EXISTS (SELECT ContaPagarNegociacaoPagamento.codigo 
    		FROM ContaPagarNegociacaoPagamento 
    		INNER JOIN negociacaopagamento ON negociacaopagamento.codigo = ContaPagarNegociacaoPagamento.negociacaocontapagar 
          	WHERE ContaPagarNegociacaoPagamento.contapagar = p_contapagar)
          	AND NOT EXISTS (SELECT contapagarnegociado.codigo 
          	FROM contapagarnegociado
          	INNER JOIN negociacaocontapagar ON negociacaocontapagar.codigo = contapagarnegociado.negociacaocontapagar 
          	WHERE contapagarnegociado.contapagar = p_contapagar)
    ) THEN 
      RETURN TRUE;
    END IF;
    IF (p_situacao = 'AP' AND NOT EXISTS (SELECT ContaPagarNegociacaoPagamento.codigo
    		FROM ContaPagarNegociacaoPagamento
    		INNER JOIN negociacaopagamento ON negociacaopagamento.codigo = ContaPagarNegociacaoPagamento.negociacaocontapagar 
          	WHERE ContaPagarNegociacaoPagamento.contapagar = p_contapagar 
          	UNION 
          	SELECT contapagarnegociado.codigo 
          	FROM contapagarnegociado
          	INNER JOIN negociacaocontapagar ON negociacaocontapagar.codigo = contapagarnegociado.negociacaocontapagar 
          	WHERE contapagarnegociado.contapagar = p_contapagar)
    ) THEN 
      RETURN TRUE;
    END IF;
   	IF (p_situacao = 'NE' AND EXISTS (SELECT contapagarnegociado.codigo 
          	FROM contapagarnegociado
          	INNER JOIN negociacaocontapagar ON negociacaocontapagar.codigo = contapagarnegociado.negociacaocontapagar 
          	WHERE contapagarnegociado.contapagar = p_contapagar)
          	AND NOT EXISTS (SELECT ContaPagarNegociacaoPagamento.codigo 
    		FROM ContaPagarNegociacaoPagamento 
    		INNER JOIN negociacaopagamento ON negociacaopagamento.codigo = ContaPagarNegociacaoPagamento.negociacaocontapagar 
          	WHERE ContaPagarNegociacaoPagamento.contapagar = p_contapagar)
    ) THEN 
      RETURN TRUE;
    END IF;
   IF (p_situacao in ('CF','PP')) THEN
      RETURN TRUE;
   END IF;
   
    RETURN v_retorno; 
END;     
$function$;

SELECT create_constraint('ALTER TABLE public.contapagar ADD CONSTRAINT check_validar_situacao_conta_pagar CHECK (fn_validarsituacaocontapagar(codigo, situacao)) NOT VALID;');
