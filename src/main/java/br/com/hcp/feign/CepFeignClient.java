package br.com.hcp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cep", url = "https://api.postmon.com.br")
public interface CepFeignClient {
	
	@GetMapping("/v1/cep/{cep}")
	public EnderecoDTO getCep(@PathVariable(name = "cep") String cep);

}
