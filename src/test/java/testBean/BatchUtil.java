package testBean;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchUtil {

    private static final int GROUP_SIZE = 500;


    /**
     * 批量更新
     *
     * @param session    session对象
     * @param executeSql 批量执行sql（占位符格式）
     * @param paramList  参数 Object[] 为一组数据
     */
    public static void batchUpdate(Session session, String executeSql, List<Object[]> paramList) {

        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try {
                    //设置手动提交事务
                    connection.setAutoCommit(false);
                    //预编译sql
                    PreparedStatement ps = connection.prepareStatement(executeSql);
                    //每500 条数据拆分一次数据
                    List<List<Object[]>> list = splitList(paramList, GROUP_SIZE);
                    //循环处理数据
                    for (List<Object[]> objects : list) {
                        //设置参数
                        for (Object[] object : objects) {
                            for (int i = 0; i < object.length; i++) {
                                ps.setObject(i + 1, object[i]);
                            }
                            //添加批处理
                            ps.addBatch();
                        }
                        //每GROUP_SIZE条数据手动提交一次事务
                        ps.executeBatch();
                        connection.commit();
                    }
                } catch (SQLException e) {
                    //发生异常，手动回滚事务
                    connection.rollback();
                    throw e;
                } finally {
                    //关闭资源
                    if (connection != null) {
                        connection.close();
                    }
                }

            }
        });
    }


    /**
     * 批量更新
     *
     * @param session    session对象
     * @param executeSql 形如where in(?) 格式的sql
     * @param inParam    参数 Object[] 为一组数据
     * @param typeName   数组对应数据库类型 例如 T = String 时  typeName = VARCHAR
     */
    public static <T> List<Map<String, Object>> batchSelect(Session session, String executeSql, List<T> inParam, String typeName) {

        List<Map<String, Object>> result = new ArrayList<>();
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try {
                    //设置手动提交事务
                    connection.setAutoCommit(false);
                    //预编译sql
                    PreparedStatement ps = connection.prepareStatement(executeSql);
                    //每500 条数据拆分一次数据
                    List<List<T>> list = splitList(inParam, GROUP_SIZE);
                    //循环处理数据
                    for (List<T> objects : list) {
                        //设置参数
                        Array array = connection.createArrayOf(typeName, objects.toArray());
                        ps.setArray(1, array);
                        ResultSet resultSet = ps.executeQuery();
                        connection.commit();
                        result.addAll(convertList(resultSet));
                    }
                } catch (SQLException e) {
                    //发生异常，手动回滚事务
                    connection.rollback();
                    throw e;
                } finally {
                    //关闭资源
                    if (connection != null) {
                        connection.close();
                    }
                }

            }
        });
        return result;
    }

    /**
     * 将比较大的list 集合每 groupSize条数据拆分一次
     *
     * @param list      源数据集合
     * @param groupSize 拆分大小
     * @return
     */
    private static <T> List<List<T>> splitList(List<T> list, int groupSize) {
        int length = list.size();
        // 计算可以分成多少组
        int num = (length + groupSize - 1) / groupSize;
        List<List<T>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = Math.min((i + 1) * groupSize, length);
            newList.add(list.subList(fromIndex, toIndex));
        }
        return newList;
    }

    /**
     * 将 ResultSet转换为list 集合
     * Map 为一条数据 key 为数据库列名称(大写)，value 为对应的值
     *
     * @param rs ResultSet
     * @return 集合
     * @throws SQLException
     */
    private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i).toUpperCase(), rs.getObject(i));
                }
                list.add(rowData);
            }
        } finally {
            if (rs != null)
                rs.close();
        }
        return list;
    }


    private BatchUtil() {
    }
}
