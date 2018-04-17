/**
 * 
 */
package com.zjy.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zjy.entity.Department;
import com.zjy.entity.Doctor;
import com.zjy.service.AdminService;
import com.zjy.service.DepartmentService;
import com.zjy.util.Constants;
import com.zjy.util.CryptographyHelper;
import com.zjy.vo.DataResult;

/**
 * @author Mervyn
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminManagementController {

	@Autowired
	AdminService adminService;
	
	@Autowired
	DepartmentService departmentService;
	
    /**
     * 新增医生
     * @param doctor
     * @return
     */
    @RequestMapping("/insertDoctor")
    public DataResult insertDoctor(@RequestBody Doctor doctor) {
    	DataResult dataResult;
    	doctor.setId();
    	doctor.setDoctorNo();
    	doctor.setDoctorSalt(CryptographyHelper.getRandomSalt());
    	doctor.setDoctorPassword(CryptographyHelper.encrypt(doctor.getDoctorPassword(), doctor.getDoctorSalt()));
    	doctor.setCreateTime();
    	doctor.setUpdateTime();
        dataResult = adminService.insert(doctor);
        return dataResult;
    }
    
    @RequestMapping("/showAddDepartment")
    public String showAddDepartment() {
    	return "admin/addDepartment";
    }
    
    /**
     * 添加部门
     * @author Mervyn
     * 
     * @param name
     * @return
     */
    @RequestMapping("/addDepartment")
    @ResponseBody
    public DataResult addDepartment(@RequestParam("name") String name) {
    	DataResult dataResult;
    	
    	Department department = new Department();
    	department.setId();
    	department.setDepartmentNo();
    	department.setDepartmentName(name);
    	department.setIsDeleted(Constants.NOT_DELETED);
    	department.setCreateTime();
    	department.setUpdateTime();
    	
    	dataResult = departmentService.insert(department);
    	
        return dataResult;
    }
    
    /**
     * 医生列表    条件查询
     * 条件：姓名，科室，状态（在职-1/离职-0），入职时间
     * @return
     */
    @RequestMapping(value="/queryDoctorList", method=RequestMethod.POST)
    @ResponseBody
    public String queryDoctorList(@RequestParam(value="name", required=false)String name,
                                  @RequestParam("depNo")String depNo,
                                  @RequestParam("status")String status,
                                  @RequestParam("startTime")String startTime,
                                  @RequestParam("endTime")String endTime,
                                  Integer pageSize,Integer pageNumber,
                                  ModelMap model) {
        pageSize = (pageSize==null?0:pageSize);
        pageNumber = (pageNumber==null?1:pageNumber);
        int a = (pageNumber-1)*pageSize;
        int b = pageSize;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("name", null==name?"":name);
        param.put("depNo", null==depNo?"":depNo);
        param.put("status", null==status?"":status);
        try {
            param.put("startTime", null==startTime||"".equals(startTime)?"":sdf.parse(startTime));
            param.put("endTime", null==endTime||"".equals(endTime)?"":sdf.parse(endTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        param.put("a", a);
        param.put("b", b);
        Map<String, Object> map = adminService.queryDoctorByPage(param);
        model.put("rows", map.get("rows"));
        model.put("total", map.get("total"));
        return "";
    }
    
}
