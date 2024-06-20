package com.springboot.controller;

import org.springframework.web.bind.annotation.RestController;


import com.springboot.Exception.DataNotFoundException;
import com.springboot.Exception.DataNotPresentException;
import com.springboot.Exception.DuplicateEmailEntryException;
import com.springboot.Exception.StudentNotExitsOfProvidedId;
import com.springboot.Response.SuccsesResponse;
import com.springboot.entity.Student;
import com.springboot.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;



@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    public StudentController() {
        System.out.println("Hey i am in controller");
    }

    @Autowired
    private StudentService service;
    SuccsesResponse succsesResponse=null;

    //@RequestMapping(value = "/operations", method = {RequestMethod.GET , RequestMethod.POST , RequestMethod.PUT , RequestMethod.DELETE})
    
    

    @PostMapping("/addstudent")
    public ResponseEntity<SuccsesResponse> addStudent(@RequestBody Student s) {
            try{
                service.insertStudent(s);
                succsesResponse = new SuccsesResponse("Student Data Added", 201, s);
                return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.CREATED);
            }
            catch(DuplicateEmailEntryException ex)
            {
                String messgae = ex.reportDuplicateEmailError();
                succsesResponse = new SuccsesResponse(messgae , 400);
                return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.BAD_REQUEST);
            }
    }

    @GetMapping("/getstudent")
    public ResponseEntity<SuccsesResponse> getStudents() {
        List<Student> sList = service.getAllStudents();
        if(sList==null)
        {
            DataNotPresentException dException = new DataNotPresentException();
            succsesResponse = new SuccsesResponse(dException.getMessage(), 404, null);
            return new ResponseEntity<>(succsesResponse, HttpStatus.NOT_FOUND);
        }else{

            succsesResponse = new SuccsesResponse("Student Data Retrived", 200, sList);
            return new ResponseEntity<>(succsesResponse, HttpStatus.OK);
        }
    }

    @DeleteMapping("/deletestudent/{id}")
    public ResponseEntity<SuccsesResponse> deleteStudent(@PathVariable("id") int id)
    {   
        try{
        service.deleteStudent(id);
        succsesResponse = new SuccsesResponse("Student Deleted Succsfully", 204);
        return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.OK);
        }
        catch(StudentNotExitsOfProvidedId ex)
        {
            succsesResponse = new SuccsesResponse(ex.getMessage(), 400, null);
            return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updatestudent/{id}")
    public ResponseEntity<SuccsesResponse> updateStudent(@RequestBody Student s , @PathVariable("id") int id)
    {  
       try{
            Student student = service.updateStudent(s, id);
            succsesResponse = new SuccsesResponse("Student Data Updated", 200, student);
            return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.OK);
       }
       catch(StudentNotExitsOfProvidedId ex)
       {    
          succsesResponse = new SuccsesResponse(ex.getMessage(), 400, null);
        return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.BAD_REQUEST);
       }
        
    }

    @GetMapping("/getstudentbyid/{id}")
    public ResponseEntity<SuccsesResponse> getStudentsById(@PathVariable("id") int id)
    {
        try{
            Optional<Student> student = service.getStudentsById(id);
            succsesResponse = new SuccsesResponse("Student Data Retrived",200,student);
            return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.OK);
        }
        catch(DataNotFoundException ex)
        {
            succsesResponse = new SuccsesResponse(ex.exceptionMessage(), 400);
             return new ResponseEntity<SuccsesResponse>(succsesResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
