package com.Nishkaiv_Apim_Devportal.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Nishkaiv_Apim_Devportal.Service.ApiService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class ApimController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/getapi/{apiId}")
    public ResponseEntity<Map<String, Object>> fetchApis(@PathVariable String apiId) {
    	
    	System.out.println("apiId"+apiId);
    	return apiService.fetchApis(apiId);
    }

}
