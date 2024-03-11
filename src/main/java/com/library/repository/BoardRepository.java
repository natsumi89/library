//package com.library.repository;
//
//import com.library.entity.Board;
//import javax.persistence.EntityManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class BoardRepository {
//    @Autowired
//    private EntityManager em;
//
//    public int findAllCnt() {
//        return ((Number) em.createQuery("select count(*) from Board")
//                .getSingleResult()).intValue();
//    }
//
//    public List<Board> findListPaging(int startIndex, int pageSize) {
//        return em.createQuery("select b from Board b", Board.class)
//                .setFirstResult(startIndex)
//                .setMaxResults(pageSize)
//                .getResultList();
//    }
//}
