package com.example.mongo.dao;


import com.example.mongo.dto.ScoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScoreMongoDAOImpl implements ScoreMongoDAO{
    //spring-data-mongodb에서 제공하는 몽고디비의 핵심클래스 - 데이터를 제어할 수 있는 기능을 제공하는 클래스
    private final MongoTemplate mongoTemplate;
    private final ScoreRepository repository;

    @Override
    public ScoreDTO findById(String value) {//기본키조회(_id)값

        return repository.findById(value).get();
    }

    @Override
    public List<ScoreDTO> findCriteria(String key, String value) {
        //Query는 조건정보를 담고있는 클래스
        //1. 선택한 key에 따른 value가 정확하게 일치하는 documnet를 검색
        System.out.println(key+"=========="+value);
        //Criteria객체는 조건을 어떤 필드에 어떻게 적용할 것인지 정보를 담고 있는 객체

        //2.key(필드)와 조건을 value와 함께 적용
        String[] data = key.split(",");
/*        Criteria criteria = new Criteria(data[0]);
        if(data[1].equals("is")){
            criteria.is(value);
        } else if (data[1].equals("gt")) {
            criteria.gt(Integer.parseInt(value));
        }else if(data[1].equals("lt")){
            criteria.lt(Integer.parseInt(value));
        }*/
        //3.Query클래스의 where 메소드를 이용해서 처
//        Query query = new Query();
//
//        query.addCriteria(Criteria.where(data[0]).lte(Integer.parseInt(value)));
        //정규표현식
        Criteria criteria = new Criteria(data[0]);
        criteria.regex(".*"+value+".*");
        Query query = new Query(criteria);


        List<ScoreDTO> list =  mongoTemplate.find(query,ScoreDTO.class,"score");
        System.out.println(list);
        return list;
    }

    @Override
    public ScoreDTO findById(String key, String value) {
        //id로 조회할 수 있도록 작업
        //search와 동일하게 조건을 적용해서 검색
        //mongoTemplate의 findOne메소드를 이용해보기
        Criteria criteria = new Criteria(key);
        criteria.is(value);
        Query query = new Query(criteria);


        ScoreDTO document =  mongoTemplate.findOne(query,ScoreDTO.class,"score");
        return document;
    }

    @Override
    public void insertDocument(ScoreDTO doc) {
        mongoTemplate.insert(doc);
    }

    @Override
    public void insertAllDocument(List<ScoreDTO> docs) {
        mongoTemplate.insertAll(docs);
    }

    @Override
    public void update(ScoreDTO document) {
        Criteria criteria = new Criteria("id");
        criteria.is(document.getId());
        Query query = new Query();

        Update update = new Update();
        update.set("dept",document.getDept());
        update.set("addr",document.getAddr());
        mongoTemplate.updateMulti(query,update,"score");
    }

    @Override
    public void test1() {

    }

    @Override
    public List<ScoreDTO> findAll() {
        System.out.println("======================================");
        List<ScoreDTO> list = mongoTemplate.findAll(ScoreDTO.class,"score");
        System.out.println(list);
        return list;
    }

    @Override
    public List<ScoreDTO> findAll(int pageNo) {
        //pagingandsortingrepositoyu의 findAll 메소드를 호출하면 페이징 처리가 된 객체를 전달받을 수 있다.
        //pagingAndSortingRepositoty의 findAll메소드 내부에서 페이징처리를 자동으로 구현해준다.
        //PageNo -> 몇번째 페이지인지
        //pageSize = > 한 페이지에 출력할 도큐먼트의 갯수
        //sort객체
        Sort sort = Sort.by("property").ascending();//오름차순
        Page<ScoreDTO> page =  repository.findAll(PageRequest.of(pageNo,10,sort));
        //Page객체에서 한페이지에 출력할 document를 꺼내서 리턴
        return page.getContent();
    }
}
