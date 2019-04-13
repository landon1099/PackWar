package service;

import java.sql.SQLException;

import utils.DataSourceUtils;
import dao.ApplyDao;
import bean.ApplyBean;

public class ApplyServiceImpl implements ApplyService {
	
	private ApplyDao applyDao;
	
	@Override
	public int insertApply(ApplyBean applyBean) {
		int result = 0;
		applyDao = new ApplyDao();
		try {
			DataSourceUtils.startTransaction();
			Long query = applyDao.selectApply(applyBean);
			if (query != 1) {
				result = applyDao.insertApply(applyBean);
			}
			if (query == 1) {
				result = applyDao.updateApply(applyBean);
			}
		} catch (SQLException e) {
			result = 0;
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				DataSourceUtils.commitAndRelease();;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
