package com.zjy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zjy.entity.Department;
import com.zjy.entity.Medicine;
import com.zjy.service.MedicineService;
import com.zjy.vo.BatchResult;
import com.zjy.vo.DataGridResult;
import com.zjy.vo.DataResult;

/** 
* @author zhoujiayi
* @version 创建时间：2018年5月2日
*/
@Controller
@RequestMapping("/medicine")
public class MedicineController {
    
    @Autowired
    MedicineService medicineService;
    
    /**
     * 新增药品
     * @param medicine
     * @return
     */
    @RequestMapping(value="/addMedicine", method = RequestMethod.POST)
    @ResponseBody
    public DataResult addMedicine(@RequestParam("medicine") Medicine medicine) {
        DataResult dataResult;
        medicine.setId();
        medicine.setMedicineNo();
        medicine.setUpdateTime();
        
        dataResult =  medicineService.addMedicine(medicine);
        
        return dataResult;
    }
    
    /**
     * 分页查询药品列表
     * @param pageSize
     * @param pageNumber
     * @param name
     * @return
     */
    @RequestMapping(value="/queryMedicineList", method = RequestMethod.POST)
    @ResponseBody
    public DataGridResult queryMedicineList(@RequestParam(value = "pageSize", required = true) int pageSize,
                                            @RequestParam(value = "pageNumber", required = true) int pageNumber,
                                            @RequestParam(value = "name") String name) {
        DataGridResult dataGridResult = medicineService.queryListByName(name, pageNumber, pageSize);
        return dataGridResult;
    }
    
    
    /**
     * 根据No查药品
     * @param medicineNo
     * @return
     */
    @RequestMapping(value="/selectByMedicineNo", method = RequestMethod.POST)
    @ResponseBody
    public Medicine selectByMedicineNo(@RequestParam("medicineNo") String medicineNo) {
        Medicine medicine = medicineService.selectByMedicineNo(medicineNo);
        return medicine;
    }
    
    
    /**
     * 删除药品
     * @param medicineNos
     * @return
     */
    @RequestMapping(value = "/deleteMedicine", method = RequestMethod.POST)
    @ResponseBody
    public BatchResult<Medicine> deleteMedicine(@RequestParam("medicineNos") String medicineNos) {
        BatchResult<Medicine> batchResult;
        
        String[] medicineNoArray = medicineNos.split(",");
        
        batchResult = medicineService.deleteByMedicineNo(medicineNoArray);
        
        return batchResult;
    }
    
    
    /**
     * 修改药品信息
     * @param medicine
     * @return
     */
    @RequestMapping(value="/updateMedicine", method = RequestMethod.POST)
    public DataResult updateMedicine(@RequestParam("medicine") Medicine medicine) {
        DataResult dataResult = new DataResult();
        //新增之前剩余的药品数量
        int before = medicine.getMedicineAmount();
        //新增的数量---本操作之后即为上次新增数量
        int newAdd = medicine.getMedicineLastAddAccount();
        //新增之后药品的数量---剩余数
        int after = before + newAdd;
        medicine.setMedicineAmount(after);
        dataResult = medicineService.updateByNo(medicine);
        return dataResult;
    }
}