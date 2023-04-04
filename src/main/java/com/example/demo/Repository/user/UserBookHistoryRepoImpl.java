package com.example.demo.Repository.user;

import com.example.demo.entity.QBook;
import com.example.demo.entity.user.QUserBookHistory;
import com.example.demo.entity.user.UserBookHistory;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
@Service
public class UserBookHistoryRepoImpl implements UserBookHistoryRepoCustom {

    private EntityManager em;

    public UserBookHistoryRepoImpl(EntityManager em) {

        this.em = em;
    }

    @Override
    public Page<UserBookHistory> findBookReadingHistory(Long userId, Pageable pageable){
        JPAQuery<UserBookHistory> queryFactory = new JPAQuery<>(em);
        QUserBookHistory userBookHistory = QUserBookHistory.userBookHistory;
        QUserBookHistory userBookHistory2 = new QUserBookHistory("userBookHistory2");
//        PathBuilder<UserBookHistory> entityPath = new PathBuilder<>(UserBookHistory.class, "userBookHistory");
//        for (Sort.Order order : pageable.getSort()) {
//            PathBuilder<Object> path = entityPath.get(order.getProperty());
//            queryFactory.orderBy(new OrderSpecifier(Order.valueOf(order.getDirection().name()), path));
//        }

        List<UserBookHistory> userBookHistories = queryFactory
                .select(userBookHistory)
                .from(userBookHistory)
                .where(userBookHistory.modifiedDate.in(
                        select(userBookHistory2.modifiedDate.max())
                                .from(userBookHistory2)
                                .where(userBookHistory2.user.id.eq(userId))
                                .groupBy(userBookHistory2.book.id)))
                .orderBy(userBookHistory.modifiedDate.desc(), QBook.book.id.asc())
                .fetch();

        return new PageImpl<>(userBookHistories);
    }

}
