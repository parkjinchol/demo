package com.demo.job;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyBatisCursorItemReader<T> implements ItemReader<T> {

    private final SqlSessionFactory sqlSessionFactory;
    private final String queryId;
    private final Map<String, Object> parameterValues;

    private SqlSession sqlSession;
    private Iterator<T> iterator;

    public MyBatisCursorItemReader(SqlSessionFactory sqlSessionFactory, String queryId, Map<String, Object> parameterValues) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.queryId = queryId;
        this.parameterValues = parameterValues;
    }

    @Override
    public T read() throws Exception {
        if (iterator == null) {
            sqlSession = sqlSessionFactory.openSession();
            List<T> results = sqlSession.selectList(queryId, parameterValues);
            iterator = results.iterator();
        }

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            close();
            return null; // 더 이상 읽을 데이터 없음
        }
    }

    public void close() {
        if (sqlSession != null) {
            sqlSession.close();
            sqlSession = null;
        }
    }
}
