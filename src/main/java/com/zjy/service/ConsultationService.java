package com.zjy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zjy.dao.DepartmentMapper;
import com.zjy.dao.DoctorMapper;
import com.zjy.dao.PatientMapper;
import com.zjy.dao.RegistrationMapper;
import com.zjy.dao.VisitMapper;
import com.zjy.entity.Consultation;
import com.zjy.entity.Department;
import com.zjy.entity.Doctor;
import com.zjy.entity.Patient;
import com.zjy.entity.Registration;

/**
 * 
 * TODO
 *
 * @author zhoujiayi
 * @version $Id: ConsultationService.java, v 0.1 2018年3月30日 上午10:33:35 zhoujiayi Exp $
 */
@Service
public class ConsultationService {
    
    @Autowired
    private RegistrationMapper rMapper;
    
    @Autowired
    private PatientMapper pMapper;
    
    @Autowired
    private DoctorMapper dMapper;
    
    @Autowired
    private DepartmentMapper depMapper;
    
    @Autowired
    private VisitMapper vMapper;

    @Transactional
    public Consultation queryConsultationByNo(String regNo) {
        // TODO Auto-generated method stub
        try {
            Consultation consultation = new Consultation();
            Registration registration = rMapper.selectRegistrationByNo(regNo);
            if(null!=registration) {
                Patient patient = pMapper.selectByPatientNo(registration.getPatientNo());
                Doctor doctor = dMapper.selectByDoctorNo(registration.getDoctorNo());
                if(null!=doctor) {
                    Department department = depMapper.selectByDeptNo(doctor.getDoctorDepartmentNo());
                    consultation.setRegistrationNo(regNo);
                    consultation.setPatientNo(registration.getPatientNo());
                    consultation.setAppointmentTime(registration.getAppointmentTime());
                    consultation.setPatientName(patient.getPatientName());
                    consultation.setPatientSex(patient.getPatientSex());
                    consultation.setPatientBirth(patient.getPatientBirth());
                    consultation.setDoctorName(doctor.getDoctorName());
                    consultation.setDepartmentName(department.getDepartmentName());
                }

                /**
                 * 要加入visit表 
                 * 还要改状态 
                 * 病例描述字段由编写病例之后再update对应数据
                 */
                registration.setStatus("2");
                vMapper.insert(registration);
            }
            return consultation;
        }catch(Exception e) {
            throw new RuntimeException("挂号信息有误，查询失败");
        }
    }

}
