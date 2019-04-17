package dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import bean.ApplyBean;
import utils.DataSourceUtils;

public class ApplyDao {
//	@Test
	public int insertApply(ApplyBean applyBean) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "INSERT INTO release_application (fbwdlj,fbblj,fbsqh) VALUES(?,?,?)";
		int result = runner.update(sql, applyBean.getSvnUpperUrl(), applyBean.getSvnUrl(), applyBean.getApply_no());
//		int result = runner.update(sql, "applyBean.getSvnUpperUrl()", "applyBean.getSvnUrl()", "applyBean.getApply_no()");
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public Long selectApply(ApplyBean applyBean) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select count(*) from release_application where fbsqh = ?";
		Long query = (Long) runner.query(sql, applyBean.getApply_no(), new ScalarHandler());
		return query;
	}

//	@Test
	public int updateApply(ApplyBean applyBean) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update release_application set fbwdlj=?,fbblj=? where fbsqh=?";
		int result = runner.update(sql, applyBean.getSvnUpperUrl(), applyBean.getSvnUrl(), applyBean.getApply_no());
//		int result = runner.update(sql, "12312", "2342", "_SIT_20190413_01");
		
		return result;
	}
//	@Test
	public void updateApply() throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "update release_application set fbwdlj=?,fbblj=? where fbsqh=?";
//		int result = runner.update(sql, applyBean.getSvnUpperUrl(), applyBean.getSvnUrl(), applyBean.getApply_no());
		int update = runner.update(sql, "2", "2", "_SIT_20190413_02");
		System.out.println(update);
	}
	
}
