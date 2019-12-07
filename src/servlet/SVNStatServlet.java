package servlet;

import bean.SVNStatBean;
import com.alibaba.fastjson.JSONObject;
import utils.DateFormatUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mjh
 * @create 2019-12-07
 */
public class SVNStatServlet extends BaseServlet {

    /**
     * 跳转svnStat页面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置默认svn统计表格
        int year = Integer.parseInt(DateFormatUtil.getYYYY());
        List<SVNStatBean> list = new ArrayList();
        SVNStatBean svnStatBean = null;
        int weeks = 0;
        int lastWeekDay = 0;
        for (int i = 1; i <= 12; i++) {
            svnStatBean = new SVNStatBean();
            int dayOfMonth = DateFormatUtil.getDayOfMonth(year, i);
            weeks = dayOfMonth / 7;
            lastWeekDay = dayOfMonth % 7;
            if (lastWeekDay > 0) {
                weeks++;
            }
            if (lastWeekDay == 0) {
                lastWeekDay = 7;
            }
            svnStatBean.setLastWeekDay(lastWeekDay);
            svnStatBean.setWeeks(weeks);
            svnStatBean.setMonth(i);
            list.add(svnStatBean);
        }
        JSONObject json = new JSONObject();
        json.put("list", list);

        Map map = new HashMap();
        map.put("01-01", "1");
        map.put("01-02", "2");
        map.put("01-03", "3");
        map.put("01-04", "4");
        map.put("01-05", "5");


        map.put("02-14", "3");
        map.put("02-15", "4");

        map.put("09-04", "3");
        map.put("09-05", "4");
        map.put("09-14", "3");
        map.put("09-15", "4");

        map.put("10-14", "3");
        map.put("10-15", "4");
        json.put("map", map);
        request.setAttribute("data", json.toString());
        this.forward(request, response, "pages/svnStat");
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void svnStat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.forward(request, response, "pages/svnStat");
    }

}
