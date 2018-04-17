package com.zjy.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zjy.entity.Doctor;
import com.zjy.entity.Patient;
import com.zjy.service.DoctorService;
import com.zjy.service.PatientService;
import com.zjy.util.Constants;
import com.zjy.util.CookieTools;

/**
 * @author Mervyn
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private	PatientService patientService;
	
	@Resource
	private	DoctorService doctorService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		System.out.println("拦截URL请求地址：" + request.getRequestURL());
		Object obj = request.getSession().getAttribute(Constants.SESSION_USER);
		System.out.println("Session中的用户：" + obj);
		
		if (obj == null){
			Cookie cookie = CookieTools.getCookie(Constants.COOKIE_NAME, request);
			if (cookie != null){
				String userNo = cookie.getValue().split(",")[0];
				
				//因为不确定是病人还是医生
				Doctor doctor = doctorService.selectByDoctorNo(userNo);
				Patient patient = patientService.selectByPatientNo(userNo);
				if (doctor != null){
					request.getSession().setAttribute(Constants.SESSION_USER, doctor);
					return true;
				} else if (patient != null) {
					request.getSession().setAttribute(Constants.SESSION_USER, patient);
					return true;
				}
				
			}
	        request.getRequestDispatcher("/fail").forward(request, response);
			return false;
		}
        return true;
	}
}