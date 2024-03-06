package com.ezenac.vue.crud;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/vue")
public class VueController 
{
   private static List<Emp> list;
   
   static {
      list = new ArrayList<Emp>();
      list.add(new Emp(11,"Smith", 2500, java.sql.Date.valueOf("2005-05-12")));
      list.add(new Emp(12,"James", 2800, java.sql.Date.valueOf("2007-10-25")));
      list.add(new Emp(13,"Mary", 3200, java.sql.Date.valueOf("2003-08-23")));
      list.add(new Emp(14,"David", 3400, java.sql.Date.valueOf("2013-06-17")));
      list.add(new Emp(15,"Laura", 4210, java.sql.Date.valueOf("2020-11-08")));
   }
   
   @GetMapping("")
   public String index()
   {
      return "VueController running...";
   }
   
   @CrossOrigin("*")
   @PostMapping("/member")  // JSON 데이터 받기 테스트
   public String getJSON(@RequestBody String strJSON)
   {
      log.info(strJSON);
      return strJSON;
   }
   
   @CrossOrigin("*")
   @GetMapping("/list")  // 목록 요청  http://localhost/vue/list
   public ResponseEntity<List<Emp>> getList()
   {
      log.info("목록 요청");
      return new ResponseEntity<List<Emp>>(list, HttpStatus.OK);
   }//  "[ {empno:xxxx, ename:xxxx}, {}, {} ]"
   
   @CrossOrigin("*")
   @GetMapping("/detail/{empno}")  // 상세정보 요청
   public ResponseEntity<Emp> getDetail(@PathVariable int empno)
   {
      Emp key = new Emp(empno);
      if(list.contains(key)) {
         Emp found = list.get(list.indexOf(key));
         return new ResponseEntity<Emp>(found, HttpStatus.OK);
      }
      return new ResponseEntity<Emp>(HttpStatus.NOT_FOUND);
   }
   
   @CrossOrigin("*")
   @DeleteMapping("/delete/{empno}")  // 상세정보 요청
   public String delete(@PathVariable int empno)
   {
      Emp key = new Emp(empno);
      if(list.contains(key)) {
         boolean deleted = list.remove(key);
         return "{\"deleted\":"+deleted+"}";
      }
      return "{\"deleted\":false}";
   }
   
   @CrossOrigin("*")
   @PutMapping("/update")  // 수정 요청
   public String update(@RequestBody String jsStr)
   {
      log.info("요청 데이터:" + jsStr);
      
      JSONParser jsp = new JSONParser();  // JSON Simple 필요함
      JSONObject jsObj = null;
      try {
         jsObj = (JSONObject) jsp.parse(jsStr);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      String sEmpno = (String)jsObj.get("empno");
      String sSal = (String)jsObj.get("salary");
      log.info("사번={}, 급여={}", sEmpno, sSal);
      
      Emp key = new Emp();
      key.setEmpno(Integer.parseInt(sEmpno));
      key.setSalary(Float.parseFloat(sSal));
      
      if(list.contains(key)) {
         Emp target = list.get(list.indexOf(key));
         target.setSalary(key.getSalary());
         return "{\"updated\":true}";
      }
      return "{\"updated\":false}";
   }
   @CrossOrigin("*")
   @PostMapping("/add")  // 폼에서 전달된 JSON 데이터 받기
   public String addEmp(@RequestBody String strJSON)
   {
      log.info("요청 데이터:" + strJSON);
      
      JSONParser jsp = new JSONParser();  // JSON Simple 필요함
      JSONObject jsObj = null;
      try {
         jsObj = (JSONObject) jsp.parse(strJSON);
      } catch (ParseException e) {
         e.printStackTrace();
      }
      String sEmpno = (String)jsObj.get("empno");
      String sEname = (String)jsObj.get("ename");
      String sSal = (String)jsObj.get("salary");
      String sHiredate = (String)jsObj.get("hiredate");
      log.info("사번={}, 이름={}, 급여={}, 입사일={}", 
               sEmpno, sEname, sSal, sHiredate);
      
      Emp emp = new Emp();
      emp.setEmpno(Integer.parseInt(sEmpno));
      emp.setEname(sEname);
      emp.setSalary(Float.parseFloat(sSal));
      emp.setHiredate(java.sql.Date.valueOf(sHiredate));
      
      if(!list.contains(emp)) {
         list.add(emp);
         return "{\"added\":true}";
      }
      return "{\"added\":false}";
   }
}