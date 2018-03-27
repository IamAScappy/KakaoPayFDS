package com.kakao.fds;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.fds.controller.V1Controller;
import com.kakao.fds.service.FraudService;
import com.kakao.fds.vo.ResultVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KakaoFdsApplicationTests {

	@InjectMocks
	private V1Controller v1Controller;
		
	private MockMvc mockMVC;

	@Mock
	private FraudService fraudService;
	
	long userId = 32445666644334443l;
	
	@Before
	public void setup() {
		this.mockMVC = MockMvcBuilders.standaloneSetup(this.v1Controller).build();
	}
	
	@Test
	public void contextLoads() throws Exception {
		// FraudService가 RuleB, RuleC를 리턴하도록 STUB 정의
		when(fraudService.search(userId)).thenReturn(Arrays.asList("RuleB", "RuleC"));
		
		MockHttpServletRequestBuilder createRequest = get(new URI("/v1/fraud/" + this.userId));
		MvcResult mvcResult = mockMVC.perform(createRequest)
									.andExpect(status().isOk())
									.andReturn();
		
		String responseText = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		ResultVO result = mapper.readValue(responseText, ResultVO.class);
		System.out.println("ContextLoad1 : Result = "+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
	}
	
	@Test
	public void contextLoads2() throws Exception {
		
		// FraudService가 아무 것도 리턴하지 않도록 STUB 정의
		when(fraudService.search(userId)).thenReturn(Arrays.asList());
		
		MockHttpServletRequestBuilder createRequest = get(new URI("/v1/fraud/" + this.userId));
		MvcResult mvcResult = mockMVC.perform(createRequest)
									.andExpect(status().isOk())
									.andReturn();
		
		String responseText = mvcResult.getResponse().getContentAsString();
		
		ObjectMapper mapper = new ObjectMapper();
		ResultVO result = mapper.readValue(responseText, ResultVO.class);
		System.out.println("ContextLoad2 : Result = "+ mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
	}
	
}
