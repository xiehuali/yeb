package com.syeet.server.config;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 *Swagger2配置类
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@ConditionalOnProperty(value = { "knife4j.enable" }, matchIfMissing = true) //开启knife4j注解
public class Swagger2Config {

	//规定扫描哪些包下面生成swagger2文档
	@Bean(value = "groupRestApi")
	public Docket groupRestApi() {
		//文档类型swaagger2
		return new Docket(DocumentationType.SWAGGER_2)
				//文档名groupApiInfo（自定义）
				.apiInfo(groupApiInfo())
				//分组名称
				.groupName("分组接口")
				//选择扫描哪个包
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.syeet.server.controller"))
				//所有的路径都可以
				.paths(PathSelectors.any())
				.build()
				//给swagger2令牌，就是在sawwgger文档上可以设置token，每次请求自动携带token
				// 不然测试接口太繁琐，需要登录会被拦截
				.securityContexts(securityContexts())//全局
				.securitySchemes(securitySchemes());//安全计划
	}

	private ApiInfo groupApiInfo() {

		return new ApiInfoBuilder()
				.title("云e办API文档")
				.description("云E办(后端) 项目介绍 本项目目的是实现中小型企业的在线办公系统，云E办在线办公系统是一个用来管理日常的办公事务的 一个系统，他能够管的内容有：日常的各种流程审批，新闻，通知，公告，文件信息，财务，人事，费 用，资产，行政，项目，移动办公等等。它的作用" +
						"就是通过软件的方式，方便管理，更加简单，更加扁 平。更加高效，更加规范，能够提高整体的管理运营水平。 本项目在技术方面采用最主流的前后端分离开发模式，使用业界最流行、社区非常活跃的开源框架 Spring Boot" +
						"来构建后端，旨在实现云E办在线办公系统。包括职位管理、职称管理、部门管理、员工管 理、工资管理、在线聊天等模块。项目中还会使用业界主流的第三方组件扩展大家的知识面和技能池。 本项目主要模块及技术点如图")
				// .title("swagger-bootstrap-ui很棒~~~！！！")
				// .description("<div
				// style='font-size:14px;color:red;'>swagger-bootstrap-ui-demo RESTful
				// APIs</div>")
				// .termsOfServiceUrl("http://www.group.com/")
				// .contact("group@qq.com")
				.version("1.0")
				.build();
	}

	private List<ApiKey> securitySchemes(){
		//设置请求头信息
		List<ApiKey> result = new ArrayList<>();
		//令牌
		ApiKey apikey = new ApiKey("authorization验证","authorization","Header");
		result.add(apikey);
		return result;
	}

	private List<SecurityContext> securityContexts(){
		//设置需要登录认证的路径
		List<SecurityContext> result = new ArrayList<>();
		result.add(getContextBypath("/.*"));
		return result;
	}

	private SecurityContext getContextBypath(String pathRegex) {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex(pathRegex))
				.build();
	}

	private List<SecurityReference> defaultAuth() {
		List<SecurityReference> result = new ArrayList<>();
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		result.add(new SecurityReference("Authorization",authorizationScopes));
		return result;
	}

}