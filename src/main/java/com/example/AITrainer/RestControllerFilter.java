package com.example.AITrainer;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.example.db.DBConnectionMysql;
import com.example.db.TrainingPlanDbDeclaration;
import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.example.db.TrainingPlanTemplateEnumDbDeclaration;


@Component
//@Order(1)
//@WebFilter(urlPatterns = {"/*"})
public class RestControllerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("########## Initiating RestController filter ##########");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        if (requestURI.contains("tptemplates")) {
        	if (request.getMethod().equals(new String("GET")) &&
        			requestURI.startsWith("/tptemplates/")) {
        		response.setHeader("Access-Control-Allow-Origin", "*");
        	}
        	checkTPTConnection(TrainingPlanTemplateController.edao);
        } else if (requestURI.contains("tp")) {
        	if (request.getMethod().equals(new String("GET")) &&
        			requestURI.contains("activetp")) {
        		response.setHeader("Access-Control-Allow-Origin", "*");
        	}
        	checkTPConnection(TrainingPlanController.edaoTp);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO:
    }
    
	static void checkTPConnection(TrainingPlanDbDeclaration tp) {
    	try {
			if(tp.conn.isClosed()) {
				tp.conn = DBConnectionMysql.getInstance().getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void checkTPTConnection(TrainingPlanTemplateDbDeclaration tpt) {
    	try {
			if(tpt.conn.isClosed()) {
				tpt.conn = DBConnectionMysql.getInstance().getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}