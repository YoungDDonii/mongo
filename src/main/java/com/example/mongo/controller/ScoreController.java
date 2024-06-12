package com.example.mongo.controller;

import com.example.mongo.dto.ScoreDTO;
import com.example.mongo.service.ScoreMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController{
    private final ScoreMongoService service;


    @GetMapping("/list")
    public String mongolist(Model model){
        List<ScoreDTO> list =  service.findAll();
        model.addAttribute("mongolist",list);
        return "mongo/list";
    }

    //insert
    @GetMapping("/insert")
    public String insert(){
        return "mongo/mongo_insert";
    }

    @PostMapping("/insert")
    public String insert(ScoreDTO document){
        System.out.println("컨트롤러: " + document);
        service.insertDocument(document);
        return "redirect:/score/list";
    }
    @GetMapping("/multi/insert")
    public String multiInsert(){
        List<ScoreDTO> docs = new ArrayList<>();
        ScoreDTO documnet = null;
        for(int i=11;i<=20;i++){
            documnet = new ScoreDTO(null,"multi"+i,"multi"+i,"전산실","서울특별시",100,100);
            docs.add(documnet);
        }
        service.insertAllDocument(docs);
        return "redirect:/score/list";
    }
    @GetMapping("/paginglist")
    public String pagelist(@RequestParam("pageNo") String pageNo,Model model){
        List<ScoreDTO> pagelist =  service.findAll(Integer.parseInt(pageNo));
        System.out.println(pageNo);
        model.addAttribute("mongolist",pagelist);
        return "mongo/list";
    }
    @GetMapping("/search")
    public String sarchPage(){
        return "mongo/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("field") String field,
                         @RequestParam("criteria") String criteria,@RequestParam("value") String value,Model model){
        //List<ScoreDTO> searchlist = service.findCriteria(field+","+criteria,value);
        System.out.println("zz");
        List<ScoreDTO> searchlist = service.findCriteria(field,value);
        model.addAttribute("mongolist",searchlist);
        return "mongo/list";
    }

    @GetMapping("/read")
    public String read(@RequestParam("key") String key ,@RequestParam("value") String value,@RequestParam("action") String action,Model model){
        System.out.println(key);
        System.out.println(value);
        ScoreDTO document =  service.findById(key,value);
        String view="";
        if(action.equals("read")){
            view="mongo/mongo_detail";
        }else{
            view="mongo/mongo_update";
        }
        System.out.println(view);
        model.addAttribute("document",document);
        return view;
    }

    @GetMapping("/read2")
    public String read2(@RequestParam("value") String value,@RequestParam("action") String action,Model model){

        System.out.println(value);
        ScoreDTO document =  service.findById(value);
        String view="";
        if(action.equals("read")){
            view="mongo/mongo_detail";
        }else{
            view="mongo/mongo_update";
        }
        System.out.println(view);
        model.addAttribute("document",document);

        return view;
    }

    @PostMapping("/update")
    public String update(ScoreDTO document){
        System.out.println("&&&&&&&&&&&&"+document);
        service.update(document);
        return "redirect:/score/paginglist?pageNo=0";
    }

}
