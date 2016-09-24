package cn.lj.operate;

/**
 * 只能查询一个并且这个数据类型只能是能够从结果集影射的
 * 支持删除功能,但是参数只能是一个
 * @author as1
 *
 */
public interface OperateSql {
        Object selectOne(String sqlName);
        void delete(String sqlName);
        void add(String sqlName);
}
