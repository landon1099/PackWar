package bean;

/**
 * @author mjh
 * @create 2019-12-07
 */
public class SVNStatBean {

    private int month;  //月份
    private int weeks;  //周数
    private int lastWeekDay;  //最后一周天数

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getLastWeekDay() {
        return lastWeekDay;
    }

    public void setLastWeekDay(int lastWeekDay) {
        this.lastWeekDay = lastWeekDay;
    }
}
