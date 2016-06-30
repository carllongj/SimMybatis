package cn.lj.operate;

/**
 * 只能查询一个并且这个数据类型只能是能够从结果集影射的
 * 支持删除功能,但是参数只能是一个
 * 虽然能用,效率真的是不敢恭维,我的天呐!
 * @author as1
 *
 */
public interface OperateSql {
        Object selectOne(String sqlName);
        void delete(String sqlName);
        void add(String sqlName);
}
